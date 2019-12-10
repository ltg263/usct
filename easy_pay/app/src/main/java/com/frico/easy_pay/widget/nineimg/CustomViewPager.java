package com.frico.easy_pay.widget.nineimg;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.frico.easy_pay.utils.LogUtils;

public class CustomViewPager extends ViewPager {

    public CustomViewPager(Context context) {
        this(context, null);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            LogUtils.e("onInterceptTouchEvent: 多点触摸系统Bug");
        } catch (ArrayIndexOutOfBoundsException e) {
            LogUtils.e("onInterceptTouchEvent: 多点触摸系统Bug");
        }
        return false;
    }
}
