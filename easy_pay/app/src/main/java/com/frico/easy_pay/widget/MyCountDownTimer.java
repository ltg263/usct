package com.frico.easy_pay.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.frico.easy_pay.impl.CountDownTimerFinishedListener;
import com.frico.easy_pay.utils.UiUtils;

/**
 * 倒计时实现类
 * Created by whc on 2018/6/4.
 */
public class MyCountDownTimer extends CountDownTimer {
    private Context context;
    private String txt;
    private TextView splashTips;
    private CountDownTimerFinishedListener listener;

    /**
     * @param millisInFuture    表示以毫秒为单位 倒计时的总数
     *                          例如 millisInFuture=1000 表示1秒
     * @param countDownInterval 表示 间隔 多少微秒 调用一次 onTick 方法
     */
    public MyCountDownTimer(Context context, int millisInFuture, long countDownInterval, TextView splashTips, String txt, final CountDownTimerFinishedListener listener) {
        super(millisInFuture, countDownInterval);
        this.context = context;
        this.txt = txt;
        this.splashTips = splashTips;
        this.listener = listener;
    }

    /**
     * 倒计时结束，可实现其他UI操作
     */
    public void onFinish() {
        listener.timeFinish();
    }

    public void onTick(long millisUntilFinished) {
        if(splashTips != null) {
            splashTips.setText(UiUtils.formatLocale("%s", millisUntilFinished / 1000) + txt);
        }
    }
}