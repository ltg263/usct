package com.frico.easy_pay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.frico.easy_pay.R;
import com.frico.easy_pay.utils.KeyboardUtils;
import com.frico.easy_pay.utils.ToastUtil;
import com.frico.easy_pay.utils.UiUtils;

/**
 * 填写抢单服务费
 * Created by wanghongchuang on 2018/12/20.
 * email:844285775@qq.com
 */
public class NoStockDialog extends Dialog implements View.OnClickListener {

    private OnButtonClick mButtonClick;
    private TextView rightTv, leftTv, content;
    boolean mIsClickDismiss = true;
    private EditText etReason;
    private Context context;

    public interface OnButtonClick {
        void onNegBtnClick();

        void onPosBtnClick(String reasonContent);
    }

    public NoStockDialog(Context context, String contentTips, OnButtonClick buttonClick) {
        super(context, R.style.simpleDialog);
        this.context = context;
        init(context, contentTips, buttonClick);
    }

    public void init(Context context, String contentTips, OnButtonClick buttonClick) {
        this.mButtonClick = buttonClick;

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_no_stock, null);
        etReason = v.findViewById(R.id.et_serviceCharge);
        content = v.findViewById(R.id.content);
        rightTv = v.findViewById(R.id.confirm);
        leftTv = v.findViewById(R.id.cancel);

        rightTv.setOnClickListener(this);
        leftTv.setOnClickListener(this);

        content.setText(contentTips);

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
                if (TextUtils.isEmpty(etReason.getText().toString())) {
                    ToastUtil.showToast(context, "服务费不可为空");
                    return;
                }

                mButtonClick.onPosBtnClick(etReason.getText().toString());
            }

            if (mIsClickDismiss) {
                dismiss();
            }
        }

        UiUtils.runOnUiThread(() -> KeyboardUtils.hideSoftInput(etReason));
    }
}
