package com.frico.easy_pay.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.frico.easy_pay.R;

/**
 * 自定义Toast
 */
public class CustomToast {

    private static Toast mToast;
    private static Context mContext;
    private static View view;
    private String text;
    private int textColor;
    private int gravity;

    public CustomToast(Context mContext, String text, int textColor, int gravity) {
        this.mContext = mContext;
        this.text = text;
        this.textColor = textColor;
        this.gravity = gravity;

        initData();
    }

    public void initData() {
        cancle();
        mToast = new Toast(mContext);

        view = LayoutInflater.from(mContext).inflate(R.layout.custom_toast_layout, null);
        TextView textView = (TextView) view.findViewById(R.id.title_tv);
        if (!TextUtils.isEmpty(text)) {
            textView.setText(text);
        }

        if (textColor != 0) {
            textView.setTextColor(textColor);
        } else {
            //默认白色
            textView.setTextColor(Color.WHITE);
        }

        if (gravity != 0) {
            mToast.setGravity(gravity, 0, 0);
        } else {
            //默认，居中，显示
            mToast.setGravity(Gravity.CENTER, 0, 0);
        }

        mToast.setView(view);
        mToast.setDuration(Toast.LENGTH_SHORT);
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * 显示Toast
     */
    public void show() {
        if (mToast != null)
            mToast.show();
    }

    /**
     * 显示Toast
     */
    public void cancle() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }
}
