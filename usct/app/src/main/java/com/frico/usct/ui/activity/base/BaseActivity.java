package com.frico.usct.ui.activity.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.frico.usct.SctApp;
import com.frico.usct.dialog.LoadDialog;
import com.frico.usct.dialog.SimpleDialog;
import com.frico.usct.receiver.NetWorkChangeBroadcastReceiver;
import com.frico.usct.service.HelperNotificationListenerService;
import com.frico.usct.utils.AnalyticsUtils;
import com.frico.usct.utils.LogUtils;
import com.frico.usct.utils.StatusTextColorUtils;
import com.frico.usct.utils.UiUtils;
import com.frico.usct.widget.CustomToast;
import com.hwangjr.rxbus.RxBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import pub.devrel.easypermissions.EasyPermissions;

public abstract class BaseActivity extends AppCompatActivity {
    private static String TAG = BaseActivity.class.getSimpleName();
    private Unbinder unbinder;
    private LoadDialog dialog;
    private NetWorkChangeBroadcastReceiver receiver;
    private CustomToast mCustomToast;

    private SimpleDialog simpleDialog;


    public String[] params = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && UiUtils.isTranslucentOrFloating(this)) {
            UiUtils.fixOrientation(this);
            LogUtils.i("api 26 全屏横竖屏切换 crash");
        }
        super.onCreate(savedInstanceState);
        LogUtils.w("当前的Activity：", getClass().getSimpleName());
        SctApp.getInstance().addActivity(this);
        registerNetChangeReceiver();
        RxBus.get().register(this);
        setContentView(setLayout());
        unbinder = ButterKnife.bind(this);
        initStatusBar();
        initTitle();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsUtils.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AnalyticsUtils.onPause(this);
        toBackground();
    }






    /**
     * 设置状态栏
     */
    private void initStatusBar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 设置状态栏背景色及文字颜色格调（如：白底黑字）效果
        StatusTextColorUtils.setStatusBar(this, false, false);
        StatusTextColorUtils.setStatusTextColor(true, this);
    }

    /**
     * 注册网络状态通知接受器
     */
    private void registerNetChangeReceiver() {
        receiver = NetWorkChangeBroadcastReceiver.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, intentFilter);
    }

    /**
     * 显示网络加载弹窗
     */
    public void show(Context context, String tips) {
        dialog = new LoadDialog(this, tips);
        if (!((Activity) context).isFinishing()) {
            dialog.show();
        }
    }

    /**
     * 显示网络加载弹窗
     */
    public void showNoTips(Context context) {
        dialog = new LoadDialog(this);
        if (!((Activity) context).isFinishing()) {
            dialog.show();
        }
    }

    /**
     * 显示网络加载弹窗
     */
    public void showText(Context context, String textStr) {
        dialog = new LoadDialog(context, textStr);
        if (!((Activity) context).isFinishing()) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null)
            dialog.dismiss();
    }

    public void cancleDialog() {
        if (dialog != null)
            dialog.cancel();
    }

    /**
     * 设置布局
     */
    protected abstract int setLayout();

    /**
     * 初始化标题
     */
    public abstract void initTitle();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    public void startAnimActivity(Intent intent) {
        this.startActivity(intent);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        if (getSystemVersion() >= 19) {
            //获取status_bar_height资源的ID
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                //根据资源ID获取响应的尺寸值
                return getResources().getDimensionPixelSize(resourceId);
            }
        }
        return 0;
    }

    /**
     * 获取系统版本
     *
     * @return
     */
    public static int getSystemVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 不传值的跳转
     */
    public void launch(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    /**
     * 禁止EditText输入空格
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpace(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ")) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /**
     * 设置统一Toast
     *
     * @param msg
     */
    public void showCustomToast(Context context, String msg) {
        cancleCustomToast();
        mCustomToast = new CustomToast(context, msg, 0, 0);
        if (!((Activity) context).isFinishing()) {
            mCustomToast.show();
        }
    }

    /**
     * 取消Toast
     */
    public void cancleCustomToast() {
        if (mCustomToast != null) {
            mCustomToast.cancle();
            mCustomToast = null;
        }
    }

    /**
     * 关闭软键盘
     */
    public void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 重写EasyPermissions的onRequestPermissionsResult方法，获取权限结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //将结果转发到EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        LogUtils.e(" onLowMemory");
        SctApp.getInstance().removeActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SctApp.getInstance().removeActivity(this);
        RxBus.get().unregister(this);

        if (unbinder != null) {
            unbinder.unbind();
        }

        if (null != receiver) {
            receiver.onDestroy();
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    //====通知栏监听 重要权限  start===================

    /**
     * 获取通知使用权限
     */
    public void getNoticeEnable() {
        boolean isAuthor = isNotificationListenerEnabled(BaseActivity.this);
        if (!isAuthor) {
            if (simpleDialog == null) {
                simpleDialog = new SimpleDialog(BaseActivity.this, "为了更好的体验和使用APP, 需要获取通知使用权!", "提示", null, "去设置", new SimpleDialog.OnButtonClick() {
                    @Override
                    public void onNegBtnClick() {

                    }

                    @Override
                    public void onPosBtnClick() {
                        openNotificationListenSettings();
                    }
                });
                simpleDialog.setCanceledOnTouchOutside(false);
                //调用这个方法时，按对话框以外的地方和按返回键都无法响应。
                simpleDialog.setCancelable(false);
                simpleDialog.show();
            }
        } else {
            if (simpleDialog != null) {
                simpleDialog = null;
            }
            ensureCollectorRunning();
        }
    }

    /**
     * 检测通知监听服务是否被授权
     *
     * @param context
     * @return
     */
    public boolean isNotificationListenerEnabled(Context context) {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(BaseActivity.this);
        if (packageNames.contains(context.getPackageName())) {
            return true;
        }
        return false;
    }



    /**
     * 检测当前服务启动情况
     */
    private void ensureCollectorRunning() {
        ComponentName collectorComponent = new ComponentName(this, HelperNotificationListenerService.class);
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        boolean collectorRunning = false;
        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(Integer.MAX_VALUE);
        if (runningServices == null) {
            return;
        }
        for (ActivityManager.RunningServiceInfo service : runningServices) {
            if (service.service.equals(collectorComponent)) {
                if (service.pid == Process.myPid()) {
                    collectorRunning = true;
                }
            }
        }
        if (collectorRunning) {
            LogUtils.e("---检查结果 - 服务运行中-");
            return;
        }

        toggleNotificationListenerService();
    }

    /**
     * 可触发系统rebind操作 -- 重新启动服务
     */
    private void toggleNotificationListenerService() {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName(this, HelperNotificationListenerService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(
                new ComponentName(this, HelperNotificationListenerService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }


    /**
     * 打开通知监听设置页面
     */
    public void openNotificationListenSettings() {
        try {
            Intent intent;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            } else {
                intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //====通知栏监听 重要权限  end===================


    /**
     * 分享文字信息
     * @param shareContent
     */
    public void shareTextByIntent(String shareContent){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
        //切记需要使用Intent.createChooser，否则会出现别样的应用选择框，您可以试试
        shareIntent = Intent.createChooser(shareIntent, "分享到");
        startActivity(shareIntent);
    }



    public void shareImgByIntent(Bitmap bitmap){
        //由文件得到uri
        Uri imageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }


    private void shareImgByIntent(File file){
        //由文件得到uri
        Uri imageUri = Uri.fromFile(file);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    /**
     * 异步线程下载图片
     */
    Bitmap bitmap;

    /**
     * 获取网络图片
     *
     * @param imageurl 图片网络地址
     * @return Bitmap 返回位图
     */
    public Bitmap GetImageInputStream(String imageurl) {
        URL url;
        HttpURLConnection connection = null;
        Bitmap bitmap = null;
        try {
            url = new URL(imageurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(6000); //超时设置
            connection.setDoInput(true);
            connection.setUseCaches(false); //设置不使用缓存
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    class Task extends AsyncTask<String, Integer, Void> {

        protected Void doInBackground(String... params) {
            bitmap = GetImageInputStream((String) params[0]);
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Message message = new Message();
            message.what = 0x123;
            mHandelr.dispatchMessage(message);
        }
    }

    private Handler mHandelr = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            shareImgByIntent(bitmap);
        }
    };


    private String imagePath;//手机截图文件地址
    //截取屏幕的方法
    public Bitmap getCurrentShareImg() {
        Bitmap bmp = getCurrentActBitmap();
        if (bmp != null)
        {
            try {
                // 获取内置SD卡路径
                String sdCardPath = Environment.getExternalStorageDirectory().getPath();
                // 图片文件路径
                imagePath = sdCardPath + File.separator + "screenshot_sct.png";
                File file = new File(imagePath);
                FileOutputStream os = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();

            } catch (Exception e) {
                LogUtils.e("screenshot","屏幕截图异常"+ e.toString());
            }
        }

        return bmp;
    }

    public Bitmap getCurrentActBitmap(){
        // 获取屏幕
        View dView = getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bmp = dView.getDrawingCache();
        return bmp;
    }


    /**
     * 获取通知栏权限是否开启
     *
     */

        private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
        private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        @SuppressLint("NewApi")
        public static boolean isNotificationEnabled(Context context) {

            AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;

            Class appOpsClass = null;
            /* Context.APP_OPS_MANAGER */
            try {
                appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                        String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

                int value = (Integer) opPostNotificationValue.get(Integer.class);
                return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return false;
        }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public static boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device
        android.app.ActivityManager activityManager = (android.app.ActivityManager) SctApp.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = SctApp.getInstance().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (android.app.ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    /**
     * 必须在所有的Activity中的onStop中调用，建议放在BaseActivity的onPause中。
     * 进行时间统计。
     */
    public static void toBackground() {
//        if (!isAppOnForeground()) {
//            // app 进入后台 开始倒计时
//            LogUtils.e(TAG,"toBackground  开始倒计时 ==== ");
//            SctApp.getInstance().startRunTime();
//
//            SctApp.getInstance().isAppRunning = false;
//        }

    }




    @Override
    protected void onStop() {
        super.onStop();
        toBackground();
    }




}
