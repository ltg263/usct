package com.frico.easy_pay.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

import com.frico.easy_pay.impl.BaseAnimation;

public class SlideInRightAnimation implements BaseAnimation {


    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view, "translationX", view.getRootView().getWidth(), 0)
        };
    }
}
