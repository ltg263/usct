package com.frico.easy_pay.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frico.easy_pay.R;
import com.frico.easy_pay.impl.ActionBarClickListener;

/**
 * 支持渐变的 actionBar
 * Created on 2016/12/28.
 */
public final class TranslucentActionBar extends LinearLayout {

    private View layRoot;
    private View vStatusBar;
    private View layLeft;
    private View layRight;
    public TextView tvTitle;
    public TextView tvLeft;
    public TextView tvRight;
    private ImageView iconLeft;
    private ImageView iconRight;
    private ImageView iconRight2;

    public TranslucentActionBar(Context context) {
        this(context, null);
    }

    public TranslucentActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TranslucentActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        setOrientation(HORIZONTAL);
        View contentView = inflate(getContext(), R.layout.actionbar_trans, this);
        layRoot = contentView.findViewById(R.id.lay_transroot);
        vStatusBar = contentView.findViewById(R.id.v_statusbar);
        tvTitle = (TextView) contentView.findViewById(R.id.tv_actionbar_title);
        tvLeft = (TextView) contentView.findViewById(R.id.tv_actionbar_left);
        tvRight = (TextView) contentView.findViewById(R.id.tv_actionbar_right);
        iconLeft = (ImageView) contentView.findViewById(R.id.iv_actionbar_left);
        iconRight = (ImageView) contentView.findViewById(R.id.iv_actionbar_right);
        iconRight2 = contentView.findViewById(R.id.iv_actionbar_right2);
        layLeft = contentView.findViewById(R.id.lay_actionbar_left);
        layRight = contentView.findViewById(R.id.lay_actionbar_right);

    }

    /**
     * 设置状态栏高度
     *
     * @param statusBarHeight
     */
    public void setStatusBarHeight(int statusBarHeight) {
        ViewGroup.LayoutParams params = vStatusBar.getLayoutParams();
        params.height = statusBarHeight;
        vStatusBar.setLayoutParams(params);
    }

    /**
     * 设置是否需要渐变
     */
    public void setNeedTranslucent() {
        setNeedTranslucent(true, false);
    }

    /**
     * 设置是否需要渐变,并且隐藏标题
     *
     * @param translucent
     */
    public void setNeedTranslucent(boolean translucent, boolean titleInitVisibile) {
        if (translucent) {
            layRoot.setBackgroundDrawable(null);
        }
        if (!titleInitVisibile) {
            tvTitle.setVisibility(View.GONE);
            tvRight.setVisibility(View.GONE);
        }
    }

    /**
     * 设置标题
     *
     * @param strTitle
     */
    public void setTitle(String strTitle) {
        if (!TextUtils.isEmpty(strTitle)) {
            tvTitle.setText(strTitle);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
    }

    /**
     * 设置Right标题
     *
     * @param strRight
     */
    public void setRight(String strRight) {
        if (!TextUtils.isEmpty(strRight)) {
            tvRight.setText(strRight);
        } else {
            tvRight.setVisibility(View.GONE);
        }
    }

    public String getRightText(){
        return tvRight.getText().toString();
    }

    /**
     * 设置Right图片
     *
     * @param resIdLeft
     */
    public void setLeftDrawable(int resIdLeft) {
        if (resIdLeft == 0) {
            iconLeft.setVisibility(View.GONE);
        } else {
            iconLeft.setBackgroundResource(resIdLeft);
            iconLeft.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置Right图片
     *
     * @param resIdRight
     */
    public void setRightDrawable(int resIdRight) {
        if (resIdRight == 0) {
            iconRight.setVisibility(View.GONE);
        } else {
            iconRight.setBackgroundResource(resIdRight);
            iconRight.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置Right标题
     *
     * @param color
     */
    public void setRightTextColor(int color) {
        tvRight.setTextColor(getResources().getColor(color));
    }


    /**
     * 设置数据
     *
     * @param strTitle
     * @param resIdLeft
     * @param strLeft
     * @param resIdRight
     * @param strRight
     * @param listener
     */
    public void setData(String strTitle, int resIdLeft, String strLeft, int resIdRight, String strRight, final ActionBarClickListener listener) {
        if (!TextUtils.isEmpty(strTitle)) {
            tvTitle.setText(strTitle);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(strLeft)) {
            tvLeft.setText(strLeft);
            tvLeft.setVisibility(View.VISIBLE);
        } else {
            tvLeft.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(strRight)) {
            tvRight.setText(strRight);
            tvRight.setVisibility(View.VISIBLE);
        } else {
            tvRight.setVisibility(View.GONE);
        }

        if (resIdLeft == 0) {
            iconLeft.setVisibility(View.GONE);
        } else {
            iconLeft.setBackgroundResource(resIdLeft);
            iconLeft.setVisibility(View.VISIBLE);
        }

        if (resIdRight == 0) {
            iconRight.setVisibility(View.GONE);
        } else {
            iconRight.setBackgroundResource(resIdRight);
            iconRight.setVisibility(View.VISIBLE);
        }

        if (listener != null) {
            layLeft.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onLeftClick();
                }
            });
            layRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRightClick();
                }
            });
        }
    }

    public void setBackground(int res) {
        layRoot.setBackground(getResources().getDrawable(res));
    }
    public void setTvTitleColor(int color){
        tvTitle.setTextColor(color);
    }

    /**
     * 右边第二个图标
     * @param res
     * @param listener
     */
    public void addTvRight2(int res,OnClickListener listener){
        iconRight2.setVisibility(VISIBLE);
        iconRight2.setImageResource(res);
        iconRight2.setOnClickListener(listener);
    }

    public void  setBackgroundAlpha(){
        layRoot.setBackgroundColor(Color.parseColor("#00ffffff"));
    }
    public void  setColorBackground(int color){
        layRoot.setBackgroundColor(color);
    }
}
