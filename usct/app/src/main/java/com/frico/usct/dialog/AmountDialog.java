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
import com.frico.usct.widget.AmountView;

/**
 * 数量的增，减及手动修改数量
 * Created by wanghongchuang on 2016/8/25.
 * email:844285775@qq.com
 */
public class AmountDialog extends Dialog implements View.OnClickListener, AmountView.OnAmountChangeListener {

    private OnButtonClick mButtonClick;
    private AmountView amountView;
    private TextView rightTv;
    private TextView leftTv;
    boolean mIsClickDismiss = true;
    private int GoodsNum;
    private EditText etAmount;

    public interface OnButtonClick {
        void onNegBtnClick();

        void onPosBtnClick(int number);
    }

    public AmountDialog(Context context, int currAmount, int stock, OnButtonClick buttonClick) {
        super(context, R.style.simpleDialog);
        init(context, currAmount, stock, buttonClick);
    }

    public void init(Context context, int currAmount, int stock, OnButtonClick buttonClick) {
        this.mButtonClick = buttonClick;

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_amount_alert, null);
        amountView = (AmountView) v.findViewById(R.id.amountView);
        etAmount = amountView.findViewById(R.id.etAmount);
        rightTv = (TextView) v.findViewById(R.id.confirm);
        leftTv = (TextView) v.findViewById(R.id.cancel);

        rightTv.setOnClickListener(this);
        leftTv.setOnClickListener(this);

        amountView.setGoods_amount(currAmount);
        amountView.setGoods_storage(stock);
        etAmount.setText("" + currAmount);

        GoodsNum = currAmount;

        etAmount.setSelection(etAmount.getText().toString().length());
        amountView.setOnAmountChangeListener(this);

        setContentView(v);
    }

    @Override
    public void onAmountChange(View view, int amount) {
        GoodsNum = amount;
    }

    @Override
    public void onClick(View v) {
        etAmount.clearFocus();
        if (v == leftTv) {
            if (mButtonClick != null) {
                mButtonClick.onNegBtnClick();
            }
        } else if (v == rightTv) {

            if (mButtonClick != null) {
                mButtonClick.onPosBtnClick(GoodsNum);
            }
        }

        UiUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                KeyboardUtils.hideSoftInput(etAmount);
            }
        });

        if (mIsClickDismiss) {
            dismiss();
        }
    }
}
