package com.frico.usct.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frico.usct.R;
import com.frico.usct.widget.CustomerKeyboard;
import com.frico.usct.widget.PasswordEditText;

/**
 * 交易密码对话框
 */
public class EnterPwdDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = "EnterPwdDialog";

    private OnButtonClick mButtonClick;

    private OnEnterCompleted completedListener;
    private TextView tvTitle;
    private TextView tvForget;
    private LinearLayout iv_back;
    private PasswordEditText passwordEditText;
    private CustomerKeyboard pay_keyboard;

    boolean mIsClickDismiss = true;

    public interface OnButtonClick {
        void onCancelBtnClick();

        void onGoChangePwdClick();
    }

    public interface OnEnterCompleted {
        void OnEnterCompleted(String content);
    }

    public EnterPwdDialog(Context context, OnEnterCompleted completedListener, OnButtonClick mButtonClick) {
        super(context, R.style.CustomDialogTransparent2);
        init(context, null, completedListener, mButtonClick);
    }

    public EnterPwdDialog(Context context, String title, OnEnterCompleted completedListener, OnButtonClick mButtonClick) {
        super(context, R.style.CustomDialogTransparent2);
        init(context, title, completedListener, mButtonClick);
    }

    public void init(Context context, String title, final OnEnterCompleted completedListener, final OnButtonClick mButtonClick) {
        this.completedListener = completedListener;
        this.mButtonClick = mButtonClick;

        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.5f;
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(layoutParams);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_enter_pwd, null);
        passwordEditText = (PasswordEditText) v.findViewById(R.id.password_edit_text);
        tvTitle = (TextView) v.findViewById(R.id.tv_title);
        tvForget = (TextView) v.findViewById(R.id.tv_forget);
        iv_back = (LinearLayout) v.findViewById(R.id.iv_back);

        passwordEditText.setFocusable(false);
        passwordEditText.setEnabled(false);
        pay_keyboard = (CustomerKeyboard) v.findViewById(R.id.pay_keyboard);//键盘

        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }

        pay_keyboard.setOnCustomerKeyboardClickListener(new CustomerKeyboard.CustomerKeyboardClickListener() {
            @Override
            public void click(String number) {
                passwordEditText.addPassword(number);
            }

            @Override
            public void delete() {
                passwordEditText.deleteLastPassword();
            }
        });

        passwordEditText.setOnPasswordFullListener(new PasswordEditText.PasswordFullListener() {
            @Override
            public void passwordFull(String password) {
                if (!TextUtils.isEmpty(password) && password.length() == 6) {
                    completedListener.OnEnterCompleted(password);

//                    if (mIsClickDismiss) {
//                        dismiss();
//                    }
                }
            }
        });

        iv_back.setOnClickListener(this);
        tvForget.setOnClickListener(this);
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
            case R.id.tv_forget:
                if (mButtonClick != null) {
                    mButtonClick.onGoChangePwdClick();
                }
                break;
        }
    }
}
