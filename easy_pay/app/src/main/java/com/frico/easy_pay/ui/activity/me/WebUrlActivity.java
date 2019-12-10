package com.frico.easy_pay.ui.activity.me;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.frico.easy_pay.R;
import com.frico.easy_pay.config.Constant;
import com.frico.easy_pay.impl.ActionBarClickListener;
import com.frico.easy_pay.ui.activity.ShareImgActivity;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.utils.LogUtils;
import com.frico.easy_pay.utils.Prefer;
import com.frico.easy_pay.utils.ToastUtil;
import com.frico.easy_pay.widget.TranslucentActionBar;
import com.tencent.smtt.sdk.CacheManager;
import com.tencent.smtt.sdk.CookieSyncManager;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class WebUrlActivity extends BaseActivity implements ActionBarClickListener, EasyPermissions.PermissionCallbacks {
    protected static String KEY_TITLE = "title";
    protected static String KEY_URL = "url";
    protected static String KEY_TYPE = "type";
    public static final int TYPE_MSG_LIST = 1;
    public static final int TYPE_MSG_1V1 = 2;
    public static final int TYPE_MSG_SERVICE = 3;


    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.load_more_loading_view)
    LinearLayout loadMoreLoadingView;
    @BindView(R.id.load_error_view)
    LinearLayout loadErrorView;

    private String url, title;
    public int mType;//区分是 客服聊天、聊天列表、1v1聊天

    private boolean mIsCanBack;


    public static void start(Activity activity,boolean isGoBack,String title,String url){
        Intent intent = new Intent(activity,WebUrlActivity.class);
        intent.putExtra(KEY_TITLE,title);
        intent.putExtra(KEY_URL,url);
        activity.startActivity(intent);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_web_url;
    }

    @Override
    public void initTitle() {
        title = getIntent().getStringExtra(Constant.TITLE);
        url = getIntent().getStringExtra(Constant.URL);
        mType = getIntent().getIntExtra(KEY_TYPE,0);
        actionbar.setData(title, R.drawable.ic_left_back2x, null, 0, "", new ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                if(webView.canGoBack()){
                    webView.goBack();
                }else{
                    finish();
                }
            }

            @Override
            public void onRightClick() {
                if(isNeedClearCookAndDataba()) {
                    clearWebView();
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
    }

    @Override
    protected void initData() {
        if (isNetworkConnected(this)) {
            initLoadView();
        } else {
            ToastUtil.showToast(WebUrlActivity.this, "当前网络不可用，请先设置网络");
            setView(0);
        }
    }

    /**
     * 初始化加载动画
     */
    private void initLoadView() {
        webView.setVisibility(View.VISIBLE);
//        loadMoreLoadingView.setVisibility(View.VISIBLE);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
//        webView.setWebContentsDebuggingEnabled(true);

        WebSettings settings = webView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }

        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);//支持放大缩小
        settings.setDisplayZoomControls(false); //不显示缩放按钮
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);


        //setDatabasePath在API19时已经废弃，原因是因为在4.4WebView的内核已经换为了Chrome的内核，存储路径有WebView控制。
//        String databasePath = webView.getContext().getDir("databases",
//                Context.MODE_PRIVATE).getPath();
//        settings.setDatabasePath(databasePath);

        //支持自动适配
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
//        settings.setBlockNetworkImage(true);// 把图片加载放在最后来加载渲染
        settings.setAllowFileAccess(true); // 允许访问文件
        settings.setSaveFormData(true);
        settings.setGeolocationEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);/// 支持通过JS打开新窗口

        //设置加载进来的页面自适应手机屏幕
        settings.setLoadWithOverviewMode(true);

        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

//        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebChromeClient(new MyWebChromeClient());

        webView.addJavascriptInterface(this, "android");
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url == null) return false;

                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    return true;
                }

                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                Map<String, String> header = new HashMap<>();
                header.put("token", Prefer.getInstance().getToken());
                view.loadUrl(url,header);
//                super.shouldOverrideUrlLoading(view,url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                setView(1);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                setView(0);
            }
        });
    }

    /**
     * 设置View的显隐操作
     *
     * @param type
     */
    private void setView(int type) {
        if (loadMoreLoadingView != null) {
            loadMoreLoadingView.setVisibility(View.GONE);
        }

        if (type == 1) {
            if (webView != null) {
                webView.setVisibility(View.VISIBLE);
            }
        } else {
            if (loadErrorView != null) {
                loadErrorView.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 获取网络状态
     *
     * @param context
     * @return
     */
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    @OnClick(R.id.tv_again_load)
    public void onClick() {
        if (!isNetworkConnected(WebUrlActivity.this)) {
            ToastUtil.showToast(WebUrlActivity.this, "当前网络不可用，请先设置网络");
            return;
        }

        if (loadErrorView != null) {
            loadErrorView.setVisibility(View.GONE);
        }

        if (loadMoreLoadingView != null) {
            loadMoreLoadingView.setVisibility(View.VISIBLE);
        }

        initView();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(WebUrlActivity.this, perms)) {
            new AppSettingsDialog.Builder(WebUrlActivity.this).build().show();
        }
    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }



    //======提供给js调用的方法==================

    /**
     * 发送邀请码
     * @param invitationCode
     */
    @JavascriptInterface
    public void sendInvitationCode(final String invitationCode) {
        copy(invitationCode);
        ToastUtil.showToast(this,"已复制到剪切板"+ invitationCode);
    }


    /**
     * 分享文字内容
     * @param shareContent 文字内容
     */
    @JavascriptInterface
    public void shareTextContent(final String shareContent) {
        //分享文字内容
//        String shareContent2 = "分享文字内容";
        shareTextByIntent(shareContent);
//        ToastUtil.showToast(this,"分享文字内容");
    }

    /**
     * 分享图片内容
     * @param shareImgUrl
     */
    @JavascriptInterface
    public void shareImgContent(final String shareImgUrl) {
        // 分享当前截图
//        screenshotAndShare();
        ShareImgActivity.start(this,shareImgUrl);
//        ToastUtil.showToast(this,"分享图片内容");
    }


    /**
     * 复制内容到剪切板
     *
     * @param copyStr
     * @return
     */
    private boolean copy(String copyStr) {
        try {
            //获取剪贴板管理器
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText(null, copyStr);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isNeedClearCookAndDataba()){
            clearWebView();
        }
    }

    private void clearWebView(){
        File file = CacheManager.getCacheFileBaseDir();
        if (file != null && file.exists() && file.isDirectory()) {
            for(File item : file.listFiles()) {
                item.delete();
            }
            file.delete();
        }
        this.deleteDatabase("webview.db");
        this.deleteDatabase("webviewCache.db");

        WebStorage.getInstance().deleteAllData(); //清空WebView的localStorage
        clearCookie();
    }

    private void clearCookie(){
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookies(null);
            cookieManager.removeAllCookie();
            cookieManager.flush();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cookieManager.removeSessionCookies(null);
            }
            cookieManager.removeAllCookie();
            CookieSyncManager.getInstance().sync();
        }
    }

    public WebView getWebView(){
        return  webView;
    }


    public boolean isNeedClearCookAndDataba(){
        return true;
    }


    private android.webkit.ValueCallback<Uri[]> mUploadCallbackAboveL;
    private android.webkit.ValueCallback<Uri> mUploadCallbackBelow;
    private Uri imageUri;
    private int REQUEST_CODE = 1234;

    public class MyWebChromeClient extends WebChromeClient{
        /**
         * 8(Android 2.2) <= API <= 10(Android 2.3)回调此方法
         */
        private void openFileChooser(android.webkit.ValueCallback<Uri> uploadMsg) {
            LogUtils.e("WangJ", "运行方法 openFileChooser-1");
            // (2)该方法回调时说明版本API < 21，此时将结果赋值给 mUploadCallbackBelow，使之 != null
            mUploadCallbackBelow = uploadMsg;
            takePhoto();
        }

        /**
         * 11(Android 3.0) <= API <= 15(Android 4.0.3)回调此方法
         */
        public void openFileChooser(android.webkit.ValueCallback<Uri> uploadMsg, String acceptType) {
            LogUtils.e("WangJ", "运行方法 openFileChooser-2 (acceptType: " + acceptType + ")");
            // 这里我们就不区分input的参数了，直接用拍照
            openFileChooser(uploadMsg);
        }

        /**
         * 16(Android 4.1.2) <= API <= 20(Android 4.4W.2)回调此方法
         */
        public void openFileChooser(android.webkit.ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            LogUtils.e("WangJ", "运行方法 openFileChooser-3 (acceptType: " + acceptType + "; capture: " + capture + ")");
            // 这里我们就不区分input的参数了，直接用拍照
            openFileChooser(uploadMsg);
        }

        /**
         * API >= 21(Android 5.0.1)回调此方法
         */
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
            LogUtils.e("WangJ", "运行方法 onShowFileChooser");
            // (1)该方法回调时说明版本API >= 21，此时将结果赋值给 mUploadCallbackAboveL，使之 != null
            mUploadCallbackAboveL = valueCallback;
            takePhoto();
            return true;
        }
    }

    /**
     * 调用相机
     */
    private void takePhoto() {
        // 指定拍照存储位置的方式调起相机
        String filePath = Environment.getExternalStorageDirectory() + File.separator
                + Environment.DIRECTORY_PICTURES + File.separator;
        String fileName = "IMG_" + DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
        imageUri = Uri.fromFile(new File(filePath + fileName));

//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(intent, REQUEST_CODE);

        // 选择图片（不包括相机拍照）,则不用成功后发刷新图库的广播
//        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//        i.addCategory(Intent.CATEGORY_OPENABLE);
//        i.setType("image/*");
//        startActivityForResult(Intent.createChooser(i, "Image Chooser"), REQUEST_CODE);

//        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        Intent Photo = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        Intent chooserIntent = Intent.createChooser(Photo, "Image Chooser");
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});

        startActivityForResult(chooserIntent, REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            // 经过上边(1)、(2)两个赋值操作，此处即可根据其值是否为空来决定采用哪种处理方法
            if (mUploadCallbackBelow != null) {
                chooseBelow(resultCode, data);
            } else if (mUploadCallbackAboveL != null) {
                chooseAbove(resultCode, data);
            } else {
                Toast.makeText(this, "发生错误", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Android API < 21(Android 5.0)版本的回调处理
     * @param resultCode 选取文件或拍照的返回码
     * @param data 选取文件或拍照的返回结果
     */
    private void chooseBelow(int resultCode, Intent data) {
        LogUtils.e("WangJ", "返回调用方法--chooseBelow");

        if (RESULT_OK == resultCode) {
            updatePhotos();

            if (data != null) {
                // 这里是针对文件路径处理
                Uri uri = data.getData();
                if (uri != null) {
                    LogUtils.e("WangJ", "系统返回URI：" + uri.toString());
                    mUploadCallbackBelow.onReceiveValue(uri);
                } else {
                    mUploadCallbackBelow.onReceiveValue(null);
                }
            } else {
                // 以指定图像存储路径的方式调起相机，成功后返回data为空
                LogUtils.e("WangJ", "自定义结果：" + imageUri.toString());
                mUploadCallbackBelow.onReceiveValue(imageUri);
            }
        } else {
            mUploadCallbackBelow.onReceiveValue(null);
        }
        mUploadCallbackBelow = null;
    }

    /**
     * Android API >= 21(Android 5.0) 版本的回调处理
     * @param resultCode 选取文件或拍照的返回码
     * @param data 选取文件或拍照的返回结果
     */
    private void chooseAbove(int resultCode, Intent data) {
        LogUtils.e("WangJ", "返回调用方法--chooseAbove");

        if (RESULT_OK == resultCode) {
            updatePhotos();

            if (data != null) {
                // 这里是针对从文件中选图片的处理
                Uri[] results;
                Uri uriData = data.getData();
                if (uriData != null) {
                    results = new Uri[]{uriData};
                    for (Uri uri : results) {
                        LogUtils.e("WangJ", "系统返回URI：" + uri.toString());
                    }
                    mUploadCallbackAboveL.onReceiveValue(results);
                } else {
                    mUploadCallbackAboveL.onReceiveValue(null);
                }
            } else {
                LogUtils.e("WangJ", "自定义结果：" + imageUri.toString());
                mUploadCallbackAboveL.onReceiveValue(new Uri[]{imageUri});
            }
        } else {
            mUploadCallbackAboveL.onReceiveValue(null);
        }
        mUploadCallbackAboveL = null;
    }

    private void updatePhotos() {
        // 该广播即使多发（即选取照片成功时也发送）也没有关系，只是唤醒系统刷新媒体文件
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(imageUri);
        sendBroadcast(intent);
    }


}
