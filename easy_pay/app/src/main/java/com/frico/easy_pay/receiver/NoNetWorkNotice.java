package com.frico.easy_pay.receiver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.frico.easy_pay.R;

/**
 * 检测到无网络后，弹窗提示
 */
public class NoNetWorkNotice {
    private WindowManager wdm;

    private View mView;
    private WindowManager.LayoutParams params;
    private boolean isShowing;
    private Context context;

    private NoNetWorkNotice(final Context context) {
        this.context = context;
        wdm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.no_net_worke_layout, null);

        mView.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        //设置LayoutParams(全局变量）相关参数
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
    }

    public static NoNetWorkNotice getInstance(Context context) {
        if (!(((Activity) context)).isFinishing()) {
            return context == null ? null : new NoNetWorkNotice(context);
        }
        return null;
    }

    public void show() {
        if (mView == null) {
            return;
        }
        isShowing = true;
        wdm.addView(mView, params);
    }

    public void cancel() {
        isShowing = false;
        wdm.removeViewImmediate(mView);
        mView = null;
    }

    public boolean isShowing() {
        return isShowing;
    }
}
