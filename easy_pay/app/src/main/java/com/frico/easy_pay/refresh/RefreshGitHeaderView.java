package com.frico.easy_pay.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.frico.easy_pay.R;
import com.frico.easy_pay.impl.PtrUIHandler;

/**
 * 下拉刷新HeaderView
 */
public class RefreshGitHeaderView extends PtrFrameLayout implements PtrUIHandler  {

    /**
     * 提醒文本
     */
    private TextView mTvRemind;


    /**
     * 状态识别
     */
    private int mState;

    /**
     * 重置
     * 准备刷新
     * 开始刷新
     * 结束刷新
     */
    public static final int STATE_RESET = -1;
    public static final int STATE_PREPARE = 0;
    public static final int STATE_BEGIN = 1;
    public static final int STATE_FINISH = 2;


    public RefreshGitHeaderView(Context context) {
        this(context, null);
    }

    public RefreshGitHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshGitHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        View view = View.inflate(getContext(), R.layout.refresh_gif_header_view_layout, null);
        mTvRemind = (TextView) view.findViewById(R.id.tv_remind);
        setHeaderView(view);
        addPtrUIHandler(this);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        mState = STATE_RESET;
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        mState = STATE_PREPARE;
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        mState = STATE_BEGIN;
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        mState = STATE_FINISH;
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        //处理提醒字体
        switch (mState) {
            case STATE_PREPARE:
                //通过设置下拉的高度高度改变提示文字占比
                if (ptrIndicator.getCurrentPercent() < 0.9) {
                    mTvRemind.setText("下拉刷新");
                } else {
                    mTvRemind.setText("松开立即刷新");
                }
                break;
            case STATE_BEGIN:
                mTvRemind.setText("正在刷新...");
                break;
            case STATE_FINISH:
                mTvRemind.setText("刷新完成");
                break;
        }
    }
}
