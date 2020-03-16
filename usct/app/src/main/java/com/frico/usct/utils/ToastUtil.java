package com.frico.usct.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created on 2017/2/10 0010.
 */
public class ToastUtil {
    private static Toast mToast;

    private static void showAllToast(Context context, String text, int duration) {
        mToast = Toast.makeText(context, text, duration);
        mToast.setText(text);
        mToast.setDuration(duration);
        mToast.show();
    }

    public static void showToast(Context context, String text) {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }

        if (!((Activity) context).isFinishing()) {
            showAllToast(context, text, Toast.LENGTH_SHORT);
        }
    }

    public static void showToastLong(Context context, String text) {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }

        if (!((Activity) context).isFinishing()) {
            showAllToast(context, text, Toast.LENGTH_LONG);
        }
    }

    /**
     * 显示下一个土司之前先干掉上一个土司，仅仅只显示一个土司
     */
    public static void showToastOnlyOne(Context context, String text) {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        showToast(context, text);
    }
}
