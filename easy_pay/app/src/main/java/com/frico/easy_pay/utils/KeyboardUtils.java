package com.frico.easy_pay.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 键盘工具类
 */
public class KeyboardUtils {

    private KeyboardUtils() {
    }

    /**
     * 输入法是否已打开
     */
    public static boolean isActive(Context context) {
        InputMethodManager im = null;
        if (im == null) {
            im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        return im.isActive();
    }

    /**
     * 隐藏输入法
     */
    public static void hideSoftInput(View view) {
        InputMethodManager im = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 显示输入法
     */
    public static void showSoftInput(View view) {
        InputMethodManager im = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }
}
