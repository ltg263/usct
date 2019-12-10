package com.frico.easy_pay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frico.easy_pay.R;
import com.frico.easy_pay.widget.PasswordEditText;

/**
 * 输入对话框
 */
public class EnterCodeDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = "SetCodeDialog";

    private OnButtonClick mButtonClick;

    private OnEnterCompleted completedListener;
    private TextView tvTitle;
    private LinearLayout iv_back;
    private PasswordEditText passwordEditText;
    private TextView tv_pay_money;

    boolean mIsClickDismiss = true;

    public interface OnButtonClick {
        void onCancelBtnClick();
    }

    public interface OnEnterCompleted {
        void OnEnterCompleted(String content);
    }

    public EnterCodeDialog(Context context, OnEnterCompleted completedListener, OnButtonClick mButtonClick) {
        super(context, R.style.simpleDialog);
        init(context, null, null, completedListener, mButtonClick);
    }

    public EnterCodeDialog(Context context, String title, String price, OnEnterCompleted completedListener, OnButtonClick mButtonClick) {
        super(context, R.style.simpleDialog);
        init(context, title, price, completedListener, mButtonClick);
    }

    public void init(Context context, String title, String price, final OnEnterCompleted completedListener, final OnButtonClick mButtonClick) {
        this.completedListener = completedListener;
        this.mButtonClick = mButtonClick;

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_enter_code, null);
        passwordEditText = (PasswordEditText) v.findViewById(R.id.password_edit_text);
        tv_pay_money = (TextView) v.findViewById(R.id.tv_pay_money);
        tvTitle = (TextView) v.findViewById(R.id.tv_title);
        iv_back = (LinearLayout) v.findViewById(R.id.iv_back);

        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }

        if (!TextUtils.isEmpty(price)) {
            tv_pay_money.setText("¥  " + price);
        }else{
            tv_pay_money.setVisibility(View.GONE);
        }

        passwordEditText.setOnPasswordFullListener(new PasswordEditText.PasswordFullListener() {
            @Override
            public void passwordFull(String password) {
                if (!TextUtils.isEmpty(password) && password.length() == 6) {
                    completedListener.OnEnterCompleted(password);
                    if (mIsClickDismiss) {
                        dismiss();
                    }
                }
            }
        });


        iv_back.setOnClickListener(this);
        setContentView(v);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setIsDismissClick(boolean isClickDismiss) {
        mIsClickDismiss = isClickDismiss;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (mButtonClick != null) {
                    mButtonClick.onCancelBtnClick();
                }
                if (mIsClickDismiss) {
                    dismiss();
                }
                break;
        }
    }
}
