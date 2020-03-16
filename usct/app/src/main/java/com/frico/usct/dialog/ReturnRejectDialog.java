package com.frico.usct.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.frico.usct.R;
import com.frico.usct.utils.KeyboardUtils;
import com.frico.usct.utils.UiUtils;

/**
 * 填写拒绝退款原因
 * Created by wanghongchuang on 2016/8/25.
 * email:844285775@qq.com
 */
public class ReturnRejectDialog extends Dialog implements View.OnClickListener {

    private OnButtonClick mButtonClick;
    private TextView rightTv;
    private TextView leftTv;
    boolean mIsClickDismiss = true;
    private EditText etReason;

    public interface OnButtonClick {
        void onNegBtnClick();

        void onPosBtnClick(String reasonContent);
    }

    public ReturnRejectDialog(Context context, OnButtonClick buttonClick) {
        super(context, R.style.simpleDialog);
        init(context, buttonClick);
    }

    public void init(Context context, OnButtonClick buttonClick) {
        this.mButtonClick = buttonClick;

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_return_reject, null);
        etReason = v.findViewById(R.id.et_reason);
        rightTv = (TextView) v.findViewById(R.id.confirm);
        leftTv = (TextView) v.findViewById(R.id.cancel);

        rightTv.setOnClickListener(this);
        leftTv.setOnClickListener(this);

        setContentView(v);
    }

    @Override
    public void onClick(View v) {
        etReason.clearFocus();
        if (v == leftTv) {
            if (mButtonClick != null) {
                mButtonClick.onNegBtnClick();
            }

            if (mIsClickDismiss) {
                dismiss();
            }
        } else if (v == rightTv) {

            if (mButtonClick != null) {
                mButtonClick.onPosBtnClick(etReason.getText().toString());
            }
        }

        UiUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                KeyboardUtils.hideSoftInput(etReason);
            }
        });
    }

    public void clickDismiss(){
        if (mIsClickDismiss) {
            dismiss();
        }
    }
}
