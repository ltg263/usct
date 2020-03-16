package com.frico.usct.ui.activity.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.frico.usct.SctApp;

/**
 * 联系客服（在线客服）
 */
public class CallCenterActivity extends WebUrlActivity {

    public static void start(Activity activity,boolean isGoBack,String title,String url,int viewType){
        Intent intent = new Intent(activity, CallCenterActivity.class);
        intent.putExtra(KEY_TITLE,title);
        intent.putExtra(KEY_URL,url);
        intent.putExtra(KEY_TYPE,viewType);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //去除标题
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        if(mType == TYPE_MSG_LIST || mType == TYPE_MSG_1V1){
            SctApp.mCurrentViewIsMember = true;
            SctApp.mIsHaveMsgMember = false;
        }else if(mType == TYPE_MSG_SERVICE){
            SctApp.mCurrentViewIsService = true;
            SctApp.mIsHaveMsgService = false;
        }
    }

    @Override
    public void initTitle() {
        super.initTitle();

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        hideBottomUIMenu(this);
        AndroidBug5497Workaround.assistActivity(this);
    }

    @Override
    public void onLeftClick() {
        //离开时调用下js方法
//        getWebView().loadUrl("javascript:displayChatDialogByClick()");
        finish();
    }

    @Override
    public void onRightClick() {

    }


    /**
     * 180115 隐藏 魅族、Nexus、华为等底部的虚拟导航按键，避免遮挡内容
     *
     * @param activity 需要隐藏底部导航按键的Activity
     */
    public static void hideBottomUIMenu(Activity activity) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT < 19) { // lower api
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View
                    .SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    //聊天记录的页面不需要每次退出清空缓存
    @Override
    public boolean isNeedClearCookAndDataba() {
        return false;
    }

    @Override
    public void finish() {
        super.finish();
        if(mType == TYPE_MSG_LIST || mType == TYPE_MSG_1V1){
            SctApp.mCurrentViewIsMember = false;
        }else if(mType == TYPE_MSG_SERVICE){
            SctApp.mCurrentViewIsService = false;
        }
    }
}
