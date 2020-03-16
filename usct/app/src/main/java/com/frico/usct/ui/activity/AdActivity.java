package com.frico.usct.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.frico.usct.R;
import com.frico.usct.impl.CountDownTimerFinishedListener;
import com.frico.usct.ui.activity.base.BaseActivity;
import com.frico.usct.utils.Prefer;
import com.frico.usct.widget.MyCountDownTimer;

import butterknife.BindView;

public class AdActivity extends BaseActivity implements View.OnClickListener, CountDownTimerFinishedListener {

    @BindView(R.id.launch_time)
    TextView splashTips;
    @BindView(R.id.img)
    SimpleDraweeView img;

    private static final int GO_MAIN = 3000;
    private MyCountDownTimer countDownTimer;

    @Override
    protected int setLayout() {
        return R.layout.activity_ad;
    }

    @Override
    public void initTitle() {
        countDownTimer = new MyCountDownTimer(this, GO_MAIN, 1000, splashTips, "s跳过", this);
        countDownTimer.start();
        splashTips.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        img.setImageURI(Uri.parse(Prefer.getInstance().getAdImageUrl()));
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
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
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
