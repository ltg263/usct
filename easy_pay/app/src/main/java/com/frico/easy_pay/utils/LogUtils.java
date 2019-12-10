package com.frico.easy_pay.utils;

import android.util.Log;

import com.frico.easy_pay.BuildConfig;

public class LogUtils {

    private static final String TAG = BuildConfig.APPLICATION_ID.substring(
            BuildConfig.APPLICATION_ID.lastIndexOf('.') + 1);

    private LogUtils() {
    }

    public static void d(String message) {
        if (BuildConfig.DEBUG)
            Log.d(TAG, buildMessage(message));
    }

    public static void e(String message) {
        if (BuildConfig.DEBUG)
            Log.e(TAG, buildMessage(message));
    }

    public static void i(String message) {
        if (BuildConfig.DEBUG)
            Log.i(TAG, buildMessage(message));
    }

    public static void v(String message) {
        if (BuildConfig.DEBUG)
            Log.v(TAG, buildMessage(message));
    }

    public static void w(String message) {
        if (BuildConfig.DEBUG)
            Log.w(TAG, buildMessage(message));
    }


    public static void d(String tag,String message) {
        if (BuildConfig.DEBUG)
            Log.d(tag, buildMessage(message));
    }

    public static void e(String tag,String message) {
        if (BuildConfig.DEBUG)
            Log.e(tag, buildMessage(message));
    }

    public static void i(String tag,String message) {
        if (BuildConfig.DEBUG)
            Log.i(tag, buildMessage(message));
    }

    public static void v(String tag,String message) {
        if (BuildConfig.DEBUG)
            Log.v(tag, buildMessage(message));
    }

    public static void w(String tag,String message) {
        if (BuildConfig.DEBUG)
            Log.w(tag, buildMessage(message));
    }

    public static void wtf(String tag,String message) {
        if (BuildConfig.DEBUG)
            Log.wtf(tag, buildMessage(message));
    }

    public static void println(String message) {
        if (BuildConfig.DEBUG)
            Log.println(Log.INFO, TAG, message);
    }

    private static String buildMessage(String rawMessage) {
        StackTraceElement caller = new Throwable().getStackTrace()[2];
        String fullClassName = caller.getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        return className + "." + caller.getMethodName() + "(): " + rawMessage;
    }
}
