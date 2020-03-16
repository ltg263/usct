package com.frico.usct.widget.nineimg;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.frico.usct.utils.LogUtils;

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
