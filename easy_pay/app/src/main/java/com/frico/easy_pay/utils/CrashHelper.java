package com.frico.easy_pay.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告。
 * 参阅：https://github.com/Ereza/CustomActivityOnCrash
 *
 * @see MyUncaughtExceptionHandler#uncaughtException(Thread, Throwable)
 * <p>
 * Created on 2017/4/22 17:46.
 */

public final class CrashHelper {

    private static final String INTENT_EXTRA_STACK_TRACE = "intent.EXTRA_STACK_TRACE";
    private static final String INTENT_ACTION_CRASH_ACTIVITY = "intent.ACTION_CRASH_ERROR";

    //General constants
    private final static String TAG = CrashHelper.class.getSimpleName();
    private static final int MAX_STACK_TRACE_SIZE = 131071; //128 KB - 1
    private static final String FILE_NAME = ".zuixingxi/crash.trace";
    private static String crashLogPath;

    //Internal variables
    private static Application application;
    private static WeakReference<Activity> lastActivityCreated = new WeakReference<>(null);

    private static Class<? extends Activity> crashActivityClass = null;
    private static boolean isInBackground = false;

    public static void install(Context context) {
        install(context, null);
    }

    /**
     * Installs this crash tool on the application using the default error activity.
     *
     * @param context Application to use for obtaining the ApplicationContext. Must not be null.
     * @see Application
     */
    public static void install(Context context, Class<? extends Activity> clazz) {
        try {
            if (context == null) {
                LogUtils.e(TAG, "Install failed: context is null!");
                return;
            }
            application = (Application) context.getApplicationContext();
            //INSTALL!
            Thread.UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();

            String pkgName = application.getPackageName();
            LogUtils.d(TAG, "current application package name is " + pkgName);
            if (oldHandler != null && oldHandler.getClass().getName().startsWith(pkgName)) {
                LogUtils.e(TAG, "You have already installed crash tool, doing nothing!");
                return;
            }
            if (oldHandler != null && !oldHandler.getClass().getName().startsWith("com.android.internal.os")) {
                LogUtils.e(TAG, "IMPORTANT WARNING! You already have an UncaughtExceptionHandler, are you sure this is correct?");
            }

            //We define a default exception handler that does what we want so it can be called from Crashlytics/ACRA
            Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
            LogUtils.i(TAG, "Crash tool has been installed.");

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                crashLogPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FILE_NAME;
            }
        } catch (Throwable t) {
            Log.e(TAG, "An unknown error occurred while installing crash tool, it may not have been properly initialized. Please report this as a bug if needed.", t);
        }

        if (clazz != null) {
            setCrashActivityClass(clazz);
        }
    }

    /**
     * 是否在后台运行
     */
    public static boolean isInBackground() {
        return isInBackground;
    }

    public static void setIsInBackground(boolean isInBackground) {
        CrashHelper.isInBackground = isInBackground;
    }

    /**
     * Sets the error activity class to launch when a crash occurs.
     * If null,the default error activity will be used.
     */
    public static void setCrashActivityClass(Class<? extends Activity> crashClass) {
        crashActivityClass = crashClass;
    }

    /**
     * Given an Intent, returns the stack trace extra from it.
     *
     * @param intent The Intent. Must not be null.
     * @return The stacktrace, or null if not provided.
     */
    public static String getStackTraceFromIntent(Intent intent) {
        return intent.getStringExtra(INTENT_EXTRA_STACK_TRACE);
    }

    /**
     * 获取崩溃异常日志
     */
    public static String getDeviceInfo() {
        StringBuilder builder = new StringBuilder();
        PackageInfo pi = getPackageInfo();
        String dateTime = DateFormat.getDateTimeInstance().format(Calendar.getInstance(Locale.CHINA).getTime());
        String appName = pi.applicationInfo.loadLabel(application.getPackageManager()).toString();
        int[] pixels = getPixels();
        String cpu;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cpu = Arrays.deepToString(Build.SUPPORTED_ABIS);
        } else {
            //noinspection deprecation
            cpu = Build.CPU_ABI;
        }
        builder.append("Date Time: ").append(dateTime).append("\n");
        builder.append("App Version: ").append(appName).append(" v").append(pi.versionName).append("(").append(pi.versionCode).append(")\n");
        builder.append("Android OS: ").append(Build.VERSION.RELEASE).append("(").append(cpu).append(")\n");
        builder.append("Phone Model: ").append(getDeviceModelName()).append("\n");
        builder.append("Screen Pixel: ").append(pixels[0]).append("x").append(pixels[1]).append(",").append(pixels[2]).append("\n\n");
        return builder.toString();
    }

    /**
     * 获取App安装包信息
     */
    private static PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        if (info == null) {
            info = new PackageInfo();
        }
        return info;
    }

    /**
     * 获取屏幕宽高像素
     */
    private static int[] getPixels() {
        int[] pixels = new int[3];
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowMgr = (WindowManager) application.getSystemService(Context.WINDOW_SERVICE);
        windowMgr.getDefaultDisplay().getMetrics(dm);
        pixels[0] = dm.widthPixels;
        pixels[1] = dm.heightPixels;
        pixels[2] = dm.densityDpi;
        return pixels;// e.g. 1080,1920,480
    }

    /**
     * INTERNAL method that returns the device model name with correct capitalization.
     * Taken from: http://stackoverflow.com/a/12707479/1254846
     *
     * @return The device model name (i.e., "LGE Nexus 5")
     */
    private static String getDeviceModelName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    /**
     * INTERNAL method that capitalizes the first character of a string
     *
     * @param s The string to capitalize
     * @return The capitalized string
     */
    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    /**
     * 判断某个Activity是否已在AndroidManifest.xml里声明
     */
    private static boolean activityAvailable(Class<? extends Activity> clazz) {
        Intent intent = new Intent();
        intent.setClass(application, clazz);
        List<ResolveInfo> list = application.getPackageManager().queryIntentActivities(intent, 0);
        return list.size() != 0;
    }

    /**
     * @see #INTENT_ACTION_CRASH_ACTIVITY
     * 获取某个意图相关的Activity，未知在AndroidManifest.xml里声明的话将获取不到
     */
    private static Class<? extends Activity> obtainActivityByIntentAction(String action) {
        List<ResolveInfo> resolveInfos = application.getPackageManager().queryIntentActivities(
                new Intent().setAction(action), PackageManager.GET_RESOLVED_FILTER);
        if (resolveInfos != null && resolveInfos.size() > 0) {
            ResolveInfo resolveInfo = resolveInfos.get(0);
            try {
                //noinspection unchecked
                return (Class<? extends Activity>) Class.forName(resolveInfo.activityInfo.name);
            } catch (ClassNotFoundException e) {
                //Should not happen, print it to the log!
                Log.e(TAG, "Failed when resolving the error activity class via intent filter, stack trace follows!", e);
            }
        }
        return null;
    }

    /**
     * INTERNAL method that checks if the stack trace that just crashed is conflictive. This is true in the following scenarios:
     * - The application has crashed while initializing (handleBindApplication is in the stack)
     * - The error activity has crashed (activityClass is in the stack)
     *
     * @param throwable     The throwable from which the stack trace will be checked
     * @param activityClass The activity class to launch when the app crashes
     * @return true if this stack trace is conflict and the activity must not be launched, false otherwise
     */
    private static boolean isStackTraceLikelyConflict(Throwable throwable, Class<? extends Activity> activityClass) {
        do {
            StackTraceElement[] stackTrace = throwable.getStackTrace();
            for (StackTraceElement element : stackTrace) {
                if ((element.getClassName().equals("android.app.ActivityThread") && element.getMethodName().equals("handleBindApplication")) || element.getClassName().equals(activityClass.getName())) {
                    return true;
                }
            }
        } while ((throwable = throwable.getCause()) != null);
        return false;
    }

    /**
     * INTERNAL method used to guess which error activity must be called when the app crashes.
     * It will first get activities from the AndroidManifest with intent filter <action android:name="cat.ereza.customactivityoncrash.ERROR" />,
     * if it cannot find them, then it will use the default error activity.
     *
     * @return The guessed error activity class, or the default error activity if not found
     */
    private static Class<? extends Activity> guessCrashActivityClass() {
        Class<? extends Activity> resolvedActivityClass;

        //If action is defined, use that
        resolvedActivityClass = obtainActivityByIntentAction(INTENT_ACTION_CRASH_ACTIVITY);

//        //Else, get the default activity
//        if (resolvedActivityClass == null && activityAvailable(CrashActivity.class)) {
//            resolvedActivityClass = CrashActivity.class;
//        }

        return resolvedActivityClass;
    }

    /**
     * INTERNAL method that kills the current process.
     * It is used after restarting or killing the app.
     */
    private static void killCurrentProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    private static class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread thread, final Throwable throwable) {
            Log.e(TAG, "App has crashed, executing UncaughtExceptionHandler", throwable);
            final String stackTraceString = toStackTraceString(throwable);
            if (crashActivityClass == null) {
                crashActivityClass = guessCrashActivityClass();
            }
            if (crashActivityClass == null) {
                Log.i(TAG, "Your crash activity not available, must declare in AndroidManifest.xml use intent-filter action: " + INTENT_ACTION_CRASH_ACTIVITY);
                saveCrashLogToFile(stackTraceString);

                //捕捉奔溃后再发送友盟
                AnalyticsUtils.reportErrorString(application, stackTraceString);
            } else {
                if (isStackTraceLikelyConflict(throwable, crashActivityClass)) {
                    Log.i(TAG, "Your application class or your crash activity have crashed, the custom activity will not be launched!");
                } else {
                    Intent intent = new Intent(application, crashActivityClass);
                    intent.putExtra(INTENT_EXTRA_STACK_TRACE, stackTraceString);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    application.startActivity(intent);
                }
            }
            Activity lastActivity = lastActivityCreated.get();
            if (lastActivity != null) {
                LogUtils.i(TAG, "Last activity: " + lastActivity.getClass().getSimpleName());
                //We finish the activity, this solves a bug which causes infinite recursion.
                //This is unsolvable in API<14, so beware!
                //See: https://github.com/ACRA/acra/issues/42
                lastActivity.finish();
                lastActivityCreated.clear();
            }
            killCurrentProcess();
        }

        private String toStackTraceString(Throwable throwable) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            String stackTraceString = sw.toString();

            //Reduce data to 128KB so we don't get a TransactionTooLargeException when sending the intent.
            //The limit is 1MB on Android but some devices seem to have it lower.
            //See: http://developer.android.com/reference/android/os/TransactionTooLargeException.html
            //And: http://stackoverflow.com/questions/11451393/what-to-do-on-transactiontoolargeexception#comment46697371_12809171
            if (stackTraceString.length() > MAX_STACK_TRACE_SIZE) {
                String disclaimer = " [stack trace too large]";
                stackTraceString = stackTraceString.substring(0, MAX_STACK_TRACE_SIZE - disclaimer.length()) + disclaimer;
            }
            return stackTraceString;
        }

        private static void saveCrashLogToFile(final String stackTraceString) {
            if (TextUtils.isEmpty(crashLogPath)) {
                LogUtils.w(TAG, "crashLogPath is empty");
                return;
            }
            LogUtils.d(TAG, "will save crash log to " + crashLogPath);
            try {
                File file = new File(crashLogPath);
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    parentFile.mkdirs();
                }
                //此处使用子线程异步是无法保证写入内容的，因为APP已死
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
                pw.write(getDeviceInfo() + "\n" + stackTraceString);
                pw.flush();
                pw.close();
                LogUtils.i(TAG, "Save stack trace success");
            } catch (Exception e) {
                Log.w(TAG, "Save stack trace failed", e);
            }
        }

    }

}