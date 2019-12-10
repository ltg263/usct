package com.frico.easy_pay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.frico.easy_pay.R;

/**
 * Created by wanghongchuang
 * on 2016/8/25.
 * email:844285775@qq.com
 */
public class SimpleDialog extends Dialog implements View.OnClickListener {

    private OnButtonClick mButtonClick;

    private int negTextColorId;
    private int posTextColorId;

    View line1;
    TextView titleTv;
    TextView rightTv;
    TextView contentTv;
    TextView leftTv;
    boolean mIsClickDismiss = true;
    private View verLine;

    public interface OnButtonClick {
        void onNegBtnClick();

        void onPosBtnClick();
    }

    public SimpleDialog(Context context, String content) {
        super(context, R.style.simpleDialog);
        init(context, content, null, null, null, null);
    }

    public SimpleDialog(Context context, String content, OnButtonClick buttonClick) {
        super(context, R.style.simpleDialog);
        init(context, content, null, context.getString(R.string.cancel), context.getString(R.string.confirm), buttonClick);
    }

    public SimpleDialog(Context context, String content, String confirm, OnButtonClick buttonClick) {
        super(context, R.style.simpleDialog);
        init(context, content, null, null, confirm, buttonClick);
    }

    public SimpleDialog(Context context, String content, String cancel, String confirm, OnButtonClick buttonClick) {
        super(context, R.style.simpleDialog);
        init(context, content, null, cancel, confirm, buttonClick);
    }

    public SimpleDialog(Context context, String content, String title, String cancel, String confirm, OnButtonClick buttonClick) {
        super(context, R.style.simpleDialog);
        init(context, content, title, cancel, confirm, buttonClick);
    }

    public void init(Context context, String content, String title, String cancel, String confirm, OnButtonClick buttonClick) {
        this.mButtonClick = buttonClick;

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_simple_alert, null);
        titleTv = (TextView) v.findViewById(R.id.title);
        contentTv = (TextView) v.findViewById(R.id.content);
        rightTv = (TextView) v.findViewById(R.id.confirm);
        leftTv = (TextView) v.findViewById(R.id.cancel);
        verLine = v.findViewById(R.id.ver_line);
        line1 = v.findViewById(R.id.line1);
        if (title != null) {
            titleTv.setText(title);
            titleTv.setVisibility(View.VISIBLE);
            line1.setVisibility(View.GONE);
        }

        if (content != null) {
            contentTv.setText(content);
        }

        if (confirm != null) {
            rightTv.setText(confirm);
        }

        if (posTextColorId != 0) {
            rightTv.setTextColor(posTextColorId);
        }

        if (cancel != null) {
            leftTv.setText(cancel);
            leftTv.setVisibility(View.VISIBLE);
            verLine.setVisibility(View.VISIBLE);
        } else {
            leftTv.setVisibility(View.GONE);
            verLine.setVisibility(View.GONE);
        }

        if (negTextColorId != 0) {
            leftTv.setTextColor(negTextColorId);
        }

        rightTv.setOnClickListener(this);
        leftTv.setOnClickListener(this);
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
    public void onClick(View v) {
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
            dismiss();
        }
    }
}
