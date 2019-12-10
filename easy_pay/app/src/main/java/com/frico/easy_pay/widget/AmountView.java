package com.frico.easy_pay.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.frico.easy_pay.R;
import com.frico.easy_pay.utils.ToastUtil;

/**
 * 自定义组件：购买数量，带减少增加按钮
 */
public class AmountView extends LinearLayout implements View.OnClickListener, TextWatcher {

    private static final String TAG = "AmountView";
    private int amount = 0; //购买数量
    private int goods_storage = 0; //商品库存

    private OnAmountChangeListener mListener;
    private Context mContext;
    private EditText etAmount;
    private ImageButton btnDecrease;
    private ImageButton btnIncrease;
    private boolean isChange = true;

    public AmountView(Context context) {
        this(context, null);
    }

    public AmountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_amount, this);
        etAmount = (EditText) findViewById(R.id.etAmount);
        btnDecrease = (ImageButton) findViewById(R.id.btnDecrease);
        btnIncrease = (ImageButton) findViewById(R.id.btnIncrease);

        btnDecrease.setOnClickListener(this);
        btnIncrease.setOnClickListener(this);
        etAmount.addTextChangedListener(this);
    }

    public void setOnAmountChangeListener(OnAmountChangeListener onAmountChangeListener) {
        this.mListener = onAmountChangeListener;
    }

    public void setGoods_amount(int amount) {
        this.amount = amount;
    }

    public void setGoods_storage(int goods_storage) {
        this.goods_storage = goods_storage;
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(etAmount.getText().toString())) {
            amount = 0;
        } else {
            amount = Integer.parseInt(etAmount.getText().toString());
        }

        int i = v.getId();
        if (i == R.id.btnDecrease) {
            if (amount >= 1) {
                amount--;
            } else {
                ToastUtil.showToastOnlyOne(mContext, "不能再减了");
            }
        } else if (i == R.id.btnIncrease) {
            if (amount > goods_storage) {
                ToastUtil.showToastOnlyOne(mContext, "不能大于库存");
            } else {
                amount++;
            }
        }

        etAmount.setText(amount + "");
        etAmount.setSelection(etAmount.getText().toString().length());
        etAmount.clearFocus();

        if (mListener != null) {
            mListener.onAmountChange(this, amount);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!isChange)
            return;

        isChange = false;

        if (s.toString().isEmpty()) {
            isChange = true;
            return;
        }

        amount = Integer.valueOf(s.toString());
        if (amount > goods_storage) {
            amount = goods_storage;
            ToastUtil.showToastOnlyOne(mContext, "不能大于库存");
        }

        etAmount.setText(amount + "");
        etAmount.setSelection(etAmount.getText().toString().length());

        if (mListener != null) {
            mListener.onAmountChange(this, amount);
        }

        isChange = true;
    }

    public interface OnAmountChangeListener {
        void onAmountChange(View view, int amount);
    }
}
