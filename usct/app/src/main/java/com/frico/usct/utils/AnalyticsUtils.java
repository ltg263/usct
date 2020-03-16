package com.frico.usct.utils;

import android.content.Context;
import androidx.fragment.app.Fragment;

import com.frico.usct.config.Constant;
import com.umeng.analytics.MobclickAgent;

/**
 * 统计工具类，对第三方统计SDK进行包装，解耦，以方便第三方SDK的更换
 */
public class AnalyticsUtils {

    public static void init(Context context) {
        MobclickAgent.setDebugMode(Constant.DEBUG_DEV);
        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.enableEncrypt(true);
    }

    public static void onResume(Context context) {
        MobclickAgent.onResume(context);
    }

    public static void onPause(Context context) {
        MobclickAgent.onPause(context);
    }

    public static void onPageStart(Fragment fragment) {
        MobclickAgent.onPageStart(fragment.getClass().getSimpleName());
    }

    public static void onPageEnd(Fragment fragment) {
        MobclickAgent.onPageEnd(fragment.getClass().getSimpleName());
    }

    public static void onEvent(Context context, String eventId) {
        MobclickAgent.onEvent(context, eventId);
    }

    //app退出
    public static void onAppExit(Context context) {
        MobclickAgent.onKillProcess(context);
    }

    //发送错误日志友盟服务器
    public static void reportErrorString(Context context, String error) {
        MobclickAgent.reportError(context, error);
    }

    public static void reportErrorThrowable(Context context, Throwable e) {
        MobclickAgent.reportError(context, e);
    }
}
