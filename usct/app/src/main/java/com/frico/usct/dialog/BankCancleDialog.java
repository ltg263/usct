package com.frico.usct.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.frico.usct.R;
import com.frico.usct.impl.CountDownTimerFinishedListener;
import com.frico.usct.utils.CommonUtils;
import com.frico.usct.utils.UiUtils;
import com.frico.usct.widget.MyCountDownTimer;

/**
 * Created by wanghongchuang
 * on 2016/8/25.
 * email:844285775@qq.com
 */
public class BankCancleDialog extends Dialog implements View.OnClickListener, CountDownTimerFinishedListener {

    private static final int GETCODE = 60000;
    private OnButtonClick mButtonClick;

    private int negTextColorId;
    private int posTextColorId;

    EditText et_code;
    TextView get_code;
    TextView rightTv;
    TextView contentTv;
    TextView leftTv;
    boolean mIsClickDismiss = true;
    private MyCountDownTimer countDownTimer;

    public interface OnButtonClick {
        void onNegBtnClick();

        void onPosBtnClick();
    }

    public BankCancleDialog(Context context, String content) {
        super(context, R.style.simpleDialog);
        init(context, content, null, null, null);
    }

    public BankCancleDialog(Context context, String content, String confirm, OnButtonClick buttonClick) {
        super(context, R.style.simpleDialog);
        init(context, content, null, confirm, buttonClick);
    }

    public BankCancleDialog(Context context, String content, OnButtonClick buttonClick) {
        super(context, R.style.simpleDialog);
        init(context, content, context.getString(R.string.cancel), context.getString(R.string.confirm), buttonClick);
    }

    public BankCancleDialog(Context context, String content, String cancel, String confirm, OnButtonClick buttonClick) {
        super(context, R.style.simpleDialog);
        init(context, content, cancel, confirm, buttonClick);
    }

    public void init(Context context, String content, String cancel, String confirm, OnButtonClick buttonClick) {
        this.mButtonClick = buttonClick;
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_bank_cancle, null);
        contentTv = (TextView) v.findViewById(R.id.content);  //手机号
        et_code = (EditText) v.findViewById(R.id.et_code);  //验证码
        get_code = (TextView) v.findViewById(R.id.get_code);  //获取验证码

        rightTv = (TextView) v.findViewById(R.id.confirm);
        leftTv = (TextView) v.findViewById(R.id.cancel);
        if (content != null) {
            //手机号*号显示
            contentTv.setText(CommonUtils.formatCon(content, CommonUtils.ShowType.SHOW_PHONE));
        }

        if (confirm != null) {
            rightTv.setText(confirm);
        }

        if (posTextColorId != 0) {
            rightTv.setTextColor(posTextColorId);
        }

        if (cancel != null) {
            leftTv.setText(cancel);
        } else {
            leftTv.setVisibility(View.GONE);
        }

        if (negTextColorId != 0) {
            leftTv.setTextColor(negTextColorId);
        }

        rightTv.setOnClickListener(this);
        leftTv.setOnClickListener(this);
        get_code.setOnClickListener(this);
        setContentView(v);
    }

    public void setContent(String content) {
        contentTv.setText(content);
    }

    public void setPosBtnText(String text) {
        rightTv.setText(text);
    }

    public void setPosBtnTextColor(int id) {
        posTextColorId = id;
    }

    public void setNegBtnText(String text) {
        leftTv.setText(text);
    }

    public void setNegBtnTextColor(int id) {
        negTextColorId = id;
    }

    public void setIsDismissClick(boolean isClickDismiss) {
        mIsClickDismiss = isClickDismiss;
    }

    @Override
    public void timeFinish() {
        UiUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                get_code.setText(getContext().getResources().getString(R.string.get_verification_code));
                get_code.setClickable(true);
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v == get_code) {
            countDownTimer = new MyCountDownTimer(getContext(), GETCODE, 1000, get_code, getContext().getResources().getString(R.string.getCode), this);
            countDownTimer.start();
            get_code.setClickable(false);
        } else {
            if (v == leftTv) {
                if (mButtonClick != null) {
                    mButtonClick.onNegBtnClick();
                }
            } else if (v == rightTv) {
                if (mButtonClick != null) {
                    mButtonClick.onPosBtnClick();
                }
            }
            if (mIsClickDismiss) {
                if(countDownTimer != null){
                    countDownTimer.cancel();
                }
                dismiss();
            }
        }
    }
}
