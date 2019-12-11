package com.frico.easy_pay.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.frico.easy_pay.R;
import com.frico.easy_pay.impl.CountDownTimerFinishedListener;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.utils.GifUtil;
import com.frico.easy_pay.utils.LogUtils;
import com.frico.easy_pay.utils.Prefer;
import com.frico.easy_pay.widget.MyCountDownTimer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewLaunchActivity extends BaseActivity implements CountDownTimerFinishedListener {
    @BindView(R.id.iv_launch_gif)
    ImageView ivLaunchGif;
    @BindView(R.id.iv_launch_logo)
    ImageView ivLaunchLogo;

    private static final int GO_MAIN = 3000;
    private MyCountDownTimer countDownTimer;


    @Override
    protected int setLayout() {
        if (!TextUtils.isEmpty(Prefer.getInstance().getAdImageUrl())) {
            LogUtils.e("---存在广告图---");
            launch(AdActivity.class);
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            finish();
        }

        return R.layout.activity_launch_new;

    }

    @Override
    public void initTitle() {
        if (!TextUtils.isEmpty(Prefer.getInstance().getAdImageUrl())) {
            return;
        }


        //countDownTimer = new MyCountDownTimer(this, GO_MAIN, 1000, null, "", this);
        //countDownTimer.start();
        //splashTips.setOnClickListener(this);
       /* Glide.with(this).load(R.drawable.launch_gif).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if (resource  instanceof GifDrawable) {
                    //加载一次
                    ((GifDrawable)resource).setLoopCount(1);
                }
                return false;
            }

        }).into(ivLaunchGif);*/

        GifUtil.loadOneTimeGif(this, R.drawable.launch_gif, ivLaunchGif, new GifUtil.GifListener() {
            @Override
            public void gifPlayComplete() {
                ivLaunchLogo.setVisibility(View.VISIBLE);
                AlphaAnimation alphaAnimation;
                alphaAnimation = new AlphaAnimation(0, 1);
                alphaAnimation.setDuration(1000);
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        goMain();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                ivLaunchLogo.startAnimation(alphaAnimation);
            }
        });



    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void timeFinish() {
        goMain();
    }

    private void goMain() {
        LogUtils.e("Token",Prefer.getInstance().getToken());
        if (TextUtils.isEmpty(Prefer.getInstance().getToken())) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            if (!TextUtils.isEmpty(Prefer.getInstance().getAdImageUrl())) {
                startActivity(new Intent(this, AdActivity.class));
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        super.onDestroy();
    }
}
