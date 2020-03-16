package com.frico.usct.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

import com.frico.usct.impl.BaseAnimation;

public class SlideInBottomAnimation implements BaseAnimation {

    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view, "translationY", view.getMeasuredHeight(), 0)
        };
    }
}
