package com.frico.easy_pay.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.frico.easy_pay.utils.LogUtils;

/**
 * 自定义金额格式EditText
 */
public class MoneyEditText extends androidx.appcompat.widget.AppCompatEditText {
    private static final String TAG = "MoneyEditText";
    private boolean textChange;

    public MoneyEditText(Context context) {
        this(context, null);
    }

    public MoneyEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoneyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFocusable(true);
        setFocusableInTouchMode(true);
        //监听文字变化
        addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!textChange) {
                    restrictText();
                }
                textChange = false;
            }
        });
    }


    /**
     * 将小数限制为2位
     */
    private void restrictText() {
        String input = getText().toString();
        if (TextUtils.isEmpty(input)) {
            return;
        }
        if (input.contains(".")) {
            int pointIndex = input.indexOf(".");
            int totalLenth = input.length();
            int len = (totalLenth - 1) - pointIndex;
            if (len > 4) { //改变len大于的数字  就可以显示保留小数点后几位数  目前4位  不可再次输入
                input = input.substring(0, totalLenth - 1);
                textChange = true;
                setText(input);
                setSelection(input.length());
            }
        }

        //如果以小数点开始，拼接0
        if (input.trim().substring(0).equals(".")) {
            input = "0" + input;
            setText(input);
            setSelection(2);
        }

        //如果以0开始，必须后跟小数点，否则不许输入
        if (input.startsWith("0") && input.trim().length() > 1) {
            if (!input.substring(1, 2).equals(".")) {
                setText(input.subSequence(0, 1));
                setSelection(1);
                return;
            }
        }
    }

    /**
     * 获取金额 保留小数点后两位数
     */
    public String getMoneyText() {
        String money = getText().toString();
        if (!money.contains(".")) {
            return money + ".0000";
        } else {
            //如果最后一位是小数点
            if (money.endsWith(".")) {
                return money + "0000";
            } else {
                String[] split = money.split("\\.");
                LogUtils.e(split[0] + "-----" + split[1]);
                String s = split[1];
                if (s.length() == 1) {
                    return money + "000";
                } else if (s.length() == 2) {
                    return money + "00";
                } else if (s.length() == 3) {
                    return money + "0";
                } else {
                    return money;
                }
            }
        }
    }
}
