package com.frico.usct.refresh;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frico.usct.R;
import com.frico.usct.impl.PtrUIHandler;

public class RefreshHeaderView extends PtrFrameLayout implements PtrUIHandler {

    private static final String TAG = "RefreshHeaderView";
    private TextView status_text;
    private int viewHeight;
    private ImageView donghua;
    private AnimationDrawable drawable;
    private RefreshDistanceListener listener;
    /**
     * 自开始下拉 0.2倍height内是否执行了缩放，
     */
    private boolean isScale;

    public void setOnRefreshDistanceListener(RefreshDistanceListener listener) {
        this.listener = listener;
    }

    public RefreshHeaderView(Context context) {
        super(context);
        initView();
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RefreshHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        View view = View.inflate(this.getContext(), R.layout.refresh_header_view_layout, null);
        status_text = (TextView) view.findViewById(R.id.status_test);
        donghua = (ImageView) view.findViewById(R.id.donghua);

        drawable = (AnimationDrawable) donghua.getDrawable();
        setRatioOfHeaderHeightToRefresh(1.0f);

        setHeaderView(view);
        addPtrUIHandler(this);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        drawable.stop();
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        if (frame.isPullToRefresh()) {
            status_text.setText("松开即刷新...");
        } else {
            status_text.setText("下拉刷新...");
        }
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        drawable.start();
        status_text.setText("更新中...");
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        //ptrIndicator.setRatioOfHeaderHeightToRefresh(1.0f);
        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();
        if (listener != null) {
            listener.onPositionChange(currentPos);
        }
        if (viewHeight == 0)
            viewHeight = ptrIndicator.getHeaderHeight();
        float v = currentPos * 1.0f / viewHeight;
        if (v > 1) v = 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //此处防止首次下拉到0.2height时突然缩小
            if (!isScale && v <= 0.2) {
                isScale = true;
                setImgScale(0.2f);
            }
            if (v > 0.2) {
                setImgScale(v);
            }
        }

        if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                status_text.setText("下拉刷新...");
            }
        } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                status_text.setText("松开即刷新...");
            }
        }
    }

    private void setImgScale(float v) {
        donghua.setScaleX(v);
        donghua.setScaleY(v);
    }

    public interface RefreshDistanceListener {
        void onPositionChange(int currentPosY);
    }
}
