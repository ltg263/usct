package com.frico.easy_pay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Process;
import androidx.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.frico.easy_pay.config.Constant;
import com.frico.easy_pay.core.api.RetrofitUtil;
import com.frico.easy_pay.core.entity.MbpUserVO;
import com.frico.easy_pay.core.entity.Result;
import com.frico.easy_pay.core.netty.NettyHandler;
import com.frico.easy_pay.core.utils.ConfigUtil;
import com.frico.easy_pay.impl.CountDownTimerIncomeLootListener;
import com.frico.easy_pay.ui.activity.LaunchActivity;
import com.frico.easy_pay.ui.activity.LoginActivity;
import com.frico.easy_pay.ui.activity.response.JsonCityBean;
import com.frico.easy_pay.ui.activity.response.JsonRouterBean;
import com.frico.easy_pay.ui.activity.response.ScoketVO;
import com.frico.easy_pay.utils.AnalyticsUtils;
import com.frico.easy_pay.utils.CrashHelper;
import com.frico.easy_pay.utils.DisplayUtils;
import com.frico.easy_pay.utils.GetJsonDataUtil;
import com.frico.easy_pay.utils.LogUtils;
import com.frico.easy_pay.utils.Prefer;
import com.frico.easy_pay.widget.IncomeOrderCountDownTimer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import chargepail.qz.www.qzzxing.activity.ZXingLibrary;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SctApp extends MultiDexApplication implements Thread.UncaughtExceptionHandler {

    private static final String TAG = SctApp.class.getSimpleName();
    public static int mainThreadId;
    public static Handler handler;
    public static SctApp instance = null;

    public static int mHomeSelectIndex = -1;

    //缓存 全局通知对话框显示的 通知消息
    public static ScoketVO mNotificationData;

    //内存缓存用户信息
    public static MbpUserVO mUserInfoData;

    public static boolean mIsHaveMsgMember;
    //当前是否打开了会员聊天页面
    public static boolean mCurrentViewIsMember;

    public static boolean mIsHaveMsgService;
    //当前页面是否客服页面
    public static boolean mCurrentViewIsService;

    public boolean isAppRunning;
    private boolean isRunInBackground;

    public static SctApp getInstance() {
        if (instance == null) {
            instance = new SctApp();
            instance.onCreate();
        }
        return instance;
    }

    /**
     * 所打开的Activity的存储栈集合
     */
    private static Stack<Activity> activityStack = new Stack<>();
    //省、市、区
    private ArrayList<JsonCityBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<JsonCityBean.ChildrenBeanX>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<JsonCityBean.ChildrenBeanX.ChildrenBean>>> options3Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2StringItems = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3StringItems = new ArrayList<>();
    private Map<String, String> routerMap;


    private PowerManager.WakeLock mWakeLock;
    private WifiManager.WifiLock mWifiLock;

    private int appCount;


    @Override
    public void onCreate() {
        super.onCreate();
        //监听程序里所有的Activity的活动

        //ZXing必须要做的初始化工作
        ZXingLibrary.initDisplayOpinion(this);

        //Bugly
        CrashReport.initCrashReport(getApplicationContext(), Constant.BULY_ID, false);

        initBackgroundCallBack();

        this.instance = this;
        //开启电源锁
        acquireWakeLock(this);

        //捕捉奔溃
        CrashHelper.install(this);
        DisplayUtils.init(this);

        ConfigUtil.initConfig(this);

        mainThreadId = Process.myTid();
        Fresco.initialize(this);
        initImageLoader();
        handler = new Handler();

//        initJsonData();
//        initX5Web();
    }




    private void initX5Web() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                LogUtils.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        String error = this.getErrorInfo(ex);
        LogUtils.v("uncaughtException", error);

        Intent intent = new Intent(this, LaunchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        System.exit(0);
    }

    /**
     * 获取错误的信息
     *
     * @param arg1
     * @return
     */
    private String getErrorInfo(Throwable arg1) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        arg1.printStackTrace(pw);
        pw.close();
        return writer.toString();
    }

    /**
     * 获取当前主线程的id
     *
     * @return
     */
    public int getMainThreadId() {
        return mainThreadId;
    }

    public Handler getmHandler() {
        return handler;
    }

    /**
     * 配置ImageLoader
     */
    public static void initImageLoader() {
        // 显示相关的配置
        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
                .build();

        // 性能，内存配置
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(instance).threadPoolSize(3) // 配置2条线程
                .threadPriority(Thread.NORM_PRIORITY - 2) // 配置线程优先级，比UI线程低，这样更流畅点
                .memoryCacheSize(10 * 1024 * 1024) // 配置内存缓存，值为5M
                .defaultDisplayImageOptions(imageOptions) // 默认的显示相关配置。使用的时候也可以在display中使用其他的option来配置
                .build();

        ImageLoader.getInstance().init(configuration);
    }

    /**
     * 初始化解析城市数据
     */
    private void initJsonData() {

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "city-data.json");//获取assets目录下的json文件数据
        ArrayList<JsonCityBean> jsonCityBean = GetJsonDataUtil.parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonCityBean;

        for (int i = 0; i < jsonCityBean.size(); i++) {//遍历省份
            ArrayList<JsonCityBean.ChildrenBeanX> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<JsonCityBean.ChildrenBeanX.ChildrenBean>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            ArrayList<String> cityStringList = new ArrayList<>();
            ArrayList<ArrayList<String>> Province_StringAreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonCityBean.get(i).getChildren().size(); c++) {//遍历该省份的所有城市
                cityStringList.add(jsonCityBean.get(i).getChildren().get(c).getName());
                CityList.add(jsonCityBean.get(i).getChildren().get(c));

                ArrayList<JsonCityBean.ChildrenBeanX.ChildrenBean> City_AreaList = new ArrayList<>();//该城市的所有地区列表
                ArrayList<String> City_StringAreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonCityBean.get(i).getChildren().get(c).getChildren() == null || jsonCityBean.get(i).getChildren().get(c).getChildren().size() == 0) {
                    City_AreaList.add(null);
                    City_StringAreaList.add("");
                } else {
                    for (int d = 0; d < jsonCityBean.get(i).getChildren().get(c).getChildren().size(); d++) {//该城市对应地区所有数据
                        City_AreaList.add(jsonCityBean.get(i).getChildren().get(c).getChildren().get(d));//添加该城市所有地区数据

                        City_StringAreaList.add(jsonCityBean.get(i).getChildren().get(c).getChildren().get(d).getName());
                    }
                }

                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
                Province_StringAreaList.add(City_StringAreaList);
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);
            options2StringItems.add(cityStringList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
            options3StringItems.add(Province_StringAreaList);
        }
    }

    /**
     * 初始化路由path数据
     */
    private void initRouterData() {
        String JsonData = new GetJsonDataUtil().getJson(this, "arouter-data.json");//获取assets目录下的json文件数据
        ArrayList<JsonRouterBean> jsonRouterBeans = GetJsonDataUtil.parseRouterData(JsonData);//用Gson 转成实体

        routerMap = new HashMap<>();

        for (JsonRouterBean jsonRouterBean : jsonRouterBeans) {
            routerMap.put(jsonRouterBean.getRouter_key(), jsonRouterBean.getRouter_value());
        }
    }


    /**
     * 获取路由map集合，根据key获取value值
     *
     * @return
     */
    public Map<String, String> getRouterMap() {
        return routerMap;
    }

    /**
     * 往栈中添加activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (activity != null) {
            activityStack.add(activity);
        }
    }

    /**
     * 从栈中移出activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 依次销毁activity
     */
    private void finishActivity() {
        for (int i = 0; i < activityStack.size(); i++) {
            if (activityStack.get(i) != null && !activityStack.get(i).isFinishing()) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出登录（清理信息）
     */
    public void exitLoginActivity() {
        Prefer.getInstance().clearData();
        finishActivity();
        //主动 关闭长连接socket
        NettyHandler.getInstance().closeSocket();
    }

    /**
     * 跳转到登录界面
     */
    public void gotoLoginActivity() {
        //防止多次跳转打开新activity
        AnalyticsUtils.onAppExit(this);
        Prefer.getInstance().clearData();

        finishActivity();

        Intent intentLogin = new Intent();
        intentLogin.setClass(this, LoginActivity.class);
        intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentLogin);
    }

    /**
     * 退出登录
     *
     * @param activity
     */
    public void exit(Activity activity) {
        finishActivity();
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
        }
    }

    public ArrayList<JsonCityBean> getOptions1Items() {
        return options1Items;
    }

    public void setOptions1Items(ArrayList<JsonCityBean> options1Items) {
        this.options1Items = options1Items;
    }

    public ArrayList<ArrayList<JsonCityBean.ChildrenBeanX>> getOptions2Items() {
        return options2Items;
    }

    public void setOptions2Items(ArrayList<ArrayList<JsonCityBean.ChildrenBeanX>> options2Items) {
        this.options2Items = options2Items;
    }

    public ArrayList<ArrayList<ArrayList<JsonCityBean.ChildrenBeanX.ChildrenBean>>> getOptions3Items() {
        return options3Items;
    }

    public void setOptions3Items(ArrayList<ArrayList<ArrayList<JsonCityBean.ChildrenBeanX.ChildrenBean>>> options3Items) {
        this.options3Items = options3Items;
    }

    public ArrayList<ArrayList<String>> getOptions2StringItems() {
        return options2StringItems;
    }

    public void setOptions2StringItems(ArrayList<ArrayList<String>> options2StringItems) {
        this.options2StringItems = options2StringItems;
    }

    public ArrayList<ArrayList<ArrayList<String>>> getOptions3StringItems() {
        return options3StringItems;
    }

    public void setOptions3StringItems(ArrayList<ArrayList<ArrayList<String>>> options3StringItems) {
        this.options3StringItems = options3StringItems;
    }


    /**
     * 阻止系统休眠和wifi休眠
     * @param ctx
     */
    @SuppressLint("InvalidWakeLockTag")
    private void acquireWakeLock(Context ctx) {
        if (mWakeLock == null) {
//            PowerManager pMgr = (PowerManager) ctx.getSystemService(POWER_SERVICE);    //保持当前进程持续有效
//                mWakeLock = pMgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
//                        "MywWakelock.");

            PowerManager pMgr = (PowerManager) getSystemService(Context.POWER_SERVICE);
            //保持cpu一直运行，不管屏幕是否黑屏
            if (pMgr != null) {
                mWakeLock = pMgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CPUKeepRunning");
            }

            WifiManager wMgr = (WifiManager) ctx.getSystemService(WIFI_SERVICE);       //保持wifi有效
            mWifiLock = wMgr.createWifiLock("SMS Backup");
        }
        mWakeLock.acquire();
        mWifiLock.acquire();
    }

    /**
     * 结束进程的时候，释放锁
     * @param ctx
     */
    private void releaseWakeLock(Context ctx) {
        mWakeLock.release();
        mWifiLock.release();
    }


    @Override
    public void onTerminate() {
        cancelCountDownTimer();
        // 程序终止的时候执行
        LogUtils.d(TAG, "onTerminate");
        //释放锁
        releaseWakeLock(this);

        super.onTerminate();
    }


    public static boolean isInBackground() {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) SctApp.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                //前台程序
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(SctApp.getInstance().getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(SctApp.getInstance().getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }



    private IncomeOrderCountDownTimer countDownTimerKill;
    private static final int GETCODE = 20 * 1000;//倒计时20s
    private boolean mIsCountDownTimerAlive = false;

    /**
     * 拉去到列表数据不为空的时候，启动计时器（已启动就忽略）
     */
    public void startRunTime() {
        if (countDownTimerKill == null) {
            countDownTimerKill = new IncomeOrderCountDownTimer(this, GETCODE, 1000, new CountDownTimerIncomeLootListener() {
                @Override
                public void timeFinish() {
                    LogUtils.e(TAG,"toBackground  倒计时---结束 ==== ");
                    mIsCountDownTimerAlive = false;
                    // 开始主动离线，关闭收单按钮
                    closePaymentStatus();
                }

                @Override
                public void timeCountDown() {
                    LogUtils.e(TAG,"toBackground  倒计时---进行中 ==== ");
                }
            });
        }
        if (!mIsCountDownTimerAlive) {
            mIsCountDownTimerAlive = true;
            countDownTimerKill.start();
        }
    }

    //取消倒计时
    private void cancelCountDownTimer() {
        if (countDownTimerKill != null) {
            mIsCountDownTimerAlive = false;
            countDownTimerKill.cancel();
        }
    }

    /**
     * 关闭收款方式
     */
    private void closePaymentStatus() {
        LogUtils.e(TAG,"toBackground  倒计时---结束---关闭收款 ==== ");
        RetrofitUtil.getInstance().apiService()
                .changepaymentstatus("hidden")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        if (result.getCode() == 1) {
                            LogUtils.e(TAG,"toBackground  倒计时---结束---关闭收款 ---成功 ==== ");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initBackgroundCallBack() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                appCount++;
                if (isRunInBackground) {
                    //应用从后台回到前台 需要做的操作
                    back2App(activity);
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                appCount--;
                if (appCount == 0) {
                    //应用进入后台 需要做的操作
                    leaveApp(activity);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

    /**
     * 从后台回到前台需要执行的逻辑
     *
     * @param activity
     */
    private void back2App(Activity activity) {
        isRunInBackground = false;
        cancelCountDownTimer();
    }

    /**
     * 离开应用 压入后台或者退出应用
     *
     * @param activity
     */
    private void leaveApp(Activity activity) {
        isRunInBackground = true;
        //2019.10.15 18：55  根据要求，暂时关闭后台自动关闭的 逻辑
//        startRunTime();
    }






}
