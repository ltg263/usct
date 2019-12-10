package com.frico.easy_pay.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.frico.easy_pay.R;
import com.frico.easy_pay.impl.CountDownTimerFinishedListener;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.utils.LogUtils;
import com.frico.easy_pay.utils.Prefer;
import com.frico.easy_pay.widget.MyCountDownTimer;

import butterknife.BindView;

public class LaunchActivity extends BaseActivity implements View.OnClickListener, CountDownTimerFinishedListener {

    @BindView(R.id.launch_time)
    TextView splashTips;
    @BindView(R.id.img)
    SimpleDraweeView img;

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

        //判断是否需要引导页
//        if (Prefer.getInstance().getNeedGuide()) {
//            LogUtils.e("---第一次-安装---");
//            launch(GuideActivity.class);
//            Prefer.getInstance().setNeedGuide(false);
//            if (countDownTimer != null) {
//                countDownTimer.cancel();
//            }
//            finish();
//        }
        return R.layout.activity_launch;
    }

    @Override
    public void initTitle() {
//        if (Prefer.getInstance().getNeedGuide()) {
//            return;
//        }

        if (!TextUtils.isEmpty(Prefer.getInstance().getAdImageUrl())) {
            return;
        }

        countDownTimer = new MyCountDownTimer(this, GO_MAIN, 1000, splashTips, "s跳过", this);
        countDownTimer.start();
        splashTips.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        scaleImage(LaunchActivity.this, img, R.drawable.mb_guide_back4);
    }

    /**
     * 实现倒计时结束时调用方法
     */
    @Override
    public void timeFinish() {
        GoMain();
    }

    /**
     * 跳转（判断Token是否存在，跳转首页或登录页）
     */
    public void GoMain() {
        splashTips.setText(getResources().getString(R.string.jumping));
       // startActivity(new Intent(this, NewWalletActivity.class));
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
    public void onClick(View v) {
        if (v.getId() == R.id.launch_time) {
            countDownTimer.cancel();
            GoMain();
        }
    }

    /**
     * 开始对图片进行拉伸或者缩放
     *
     * @param activity
     * @param view
     * @param drawableResId
     */
    public static void scaleImage(final Activity activity, final View view, int drawableResId) {
        // 获取屏幕的高宽
        Point outSize = new Point();
        activity.getWindow().getWindowManager().getDefaultDisplay().getSize(outSize);
        // 解析将要被处理的图片
        Bitmap resourceBitmap = BitmapFactory.decodeResource(activity.getResources(), drawableResId);
        if (resourceBitmap == null) {
            view.setBackgroundResource(R.drawable.mb_guide_back4);
            return;
        }

        // 使用图片的缩放比例计算将要放大的图片的高度
        int bitmapScaledHeight = Math.round(resourceBitmap.getHeight() * outSize.x * 1.0f / resourceBitmap.getWidth());
        // 以屏幕的宽度为基准，如果图片的宽度比屏幕宽，则等比缩小，如果窄，则放大
        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(resourceBitmap, outSize.x, bitmapScaledHeight, false);

        if (scaledBitmap == null) {
            view.setBackgroundResource(R.drawable.mb_guide_back4);
            return;
        }

        view.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (scaledBitmap.getHeight() <= 0) {
                view.setBackgroundResource(R.drawable.mb_guide_back4);
                return;
            }

            // 当UI绘制完毕，我们对图片进行处理
            int viewHeight = view.getMeasuredHeight();
            // 计算将要裁剪的图片的顶部以及底部的偏移量
            int offset = (scaledBitmap.getHeight() - viewHeight) / 2;

            if (offset < 0) {
                offset = 0;
            }

            if ((scaledBitmap.getHeight() - offset * 2) <= 0) {
                view.setBackgroundResource(R.drawable.mb_guide_back4);
                return;
            }

            // 对图片以中心进行裁剪，裁剪出的图片就是非常适合做引导页的图片了
            Bitmap finallyBitmap = Bitmap.createBitmap(scaledBitmap, 0, offset, scaledBitmap.getWidth(), scaledBitmap.getHeight() - offset * 2);

            if (finallyBitmap == null) {
                view.setBackgroundResource(R.drawable.mb_guide_back4);
                return;
            }

            // 设置图片显示
            view.setBackgroundDrawable(new BitmapDrawable(activity.getResources(), finallyBitmap));
        });
    }


    /**
     * 防止用户立刻退出后又重新进入
     */
    @Override
    protected void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        super.onDestroy();
    }
}
