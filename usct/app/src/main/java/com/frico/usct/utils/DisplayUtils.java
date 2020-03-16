package com.frico.usct.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.frico.usct.SctApp;

/**
 * 显示相关总汇
 *
 * @author ll
 * @version 1.0.0
 */
public class DisplayUtils {
    private static DisplayMetrics sDisplayMetrics;

    private static final float ROUND_DIFFERENCE = 0.5f;

    /**
     * 初始化操作
     *
     * @param context context
     */
    public static void init(Context context) {
        sDisplayMetrics = context.getResources().getDisplayMetrics();
        checkScreen();
    }

    private static void checkScreen() {
        setIsLargeScreen(sDisplayMetrics.widthPixels > 1024
                || sDisplayMetrics.widthPixels / sDisplayMetrics.density >= 480);
    }

    /**
     * 获取屏幕宽度 单位：像素
     *
     * @return 屏幕宽度
     */
    public static int getWidthPixels() {
        return sDisplayMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度 单位：像素
     *
     * @return 屏幕高度
     */
    public static int getHeightPixels() {
        return sDisplayMetrics.heightPixels;
    }

    /**
     * 获取屏幕宽度 单位：像素
     *
     * @return 屏幕宽度
     */
    public static float getDensity() {
        return sDisplayMetrics.density;
    }

    /**
     * dp 转 px
     *
     * @param dp dp值
     * @return 转换后的像素值
     */
    public static int dp2px(int dp) {
        return (int) (dp * sDisplayMetrics.density + ROUND_DIFFERENCE);
    }

    /**
     * dp 转 px
     *
     * @param dp dp值
     * @return 转换后的像素值
     */
    public static float dp2px(float dp) {
        return dp * sDisplayMetrics.density + ROUND_DIFFERENCE;
    }

    /**
     * px 转 dp
     *
     * @param px px值
     * @return 转换后的dp值
     */
    public static int px2dp(int px) {
        return (int) (px / sDisplayMetrics.density + ROUND_DIFFERENCE);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(float spValue) {
        final float fontScale = SctApp.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private static boolean sIsLargeScreen = false;
    /**
     * 是否是大屏手机
     *
     * @return true - 是大屏手机
     */
    public static boolean isLargeScreen() {
        return sIsLargeScreen;
    }
    /**
     * 设置是否是大屏手机
     *
     * @param isLargeScreen 是大屏手机
     */
    public static void setIsLargeScreen(boolean isLargeScreen) {
        sIsLargeScreen = isLargeScreen;
    }
    
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
}
