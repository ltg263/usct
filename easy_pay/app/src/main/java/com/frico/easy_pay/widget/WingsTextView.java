package com.frico.easy_pay.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

/**
 * 带翅膀的TextView
 */
public class WingsTextView extends androidx.appcompat.widget.AppCompatTextView {
    final static String TAG = "WingsTextView";

    int wingsLength = 2;
    int wingsColor = Color.WHITE;

    public WingsTextView(Context context) {
        super(context);
    }

    public WingsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WingsTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setWingsLength(int wingsLength) {
        this.wingsLength = wingsLength;
        setPadding(wingsLength + getPaddingLeft(), getPaddingTop(), wingsLength + getPaddingRight(), getPaddingBottom());
    }

    public void setWingsColor(int wingsColor) {
        this.wingsColor = wingsColor;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int measuerdHeight = getMeasuredHeight();
        Paint paint = new Paint();
        paint.setColor(wingsColor);
        Rect rectLeft = new Rect(0, measuerdHeight / 2 - 1, wingsLength, measuerdHeight / 2 + 1);
        canvas.drawRect(rectLeft, paint);
        int measuredWidth = getMeasuredWidth();
        Rect rectRight = new Rect(measuredWidth - wingsLength, measuerdHeight / 2 - 1, measuredWidth, measuerdHeight / 2 + 1);
        canvas.drawRect(rectRight, paint);
    }
}
