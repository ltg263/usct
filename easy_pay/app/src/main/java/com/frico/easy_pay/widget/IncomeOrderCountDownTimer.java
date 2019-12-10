package com.frico.easy_pay.widget;

import android.content.Context;
import android.os.CountDownTimer;

import com.frico.easy_pay.impl.CountDownTimerIncomeLootListener;

/**
 * 倒计时实现类 收单
 */
public class IncomeOrderCountDownTimer extends CountDownTimer {
    private Context context;
    private CountDownTimerIncomeLootListener listener;

    /**
     * @param millisInFuture    表示以毫秒为单位 倒计时的总数
     *                          例如 millisInFuture=1000 表示1秒
     * @param countDownInterval 表示 间隔 多少微秒 调用一次 onTick 方法
     */
    public IncomeOrderCountDownTimer(Context context, int millisInFuture, long countDownInterval, final CountDownTimerIncomeLootListener listener) {
        super(millisInFuture, countDownInterval);
        this.context = context;
        this.listener = listener;
    }

    /**
     * 倒计时结束，可实现其他UI操作
     */
    public void onFinish() {
        listener.timeFinish();
    }

    public void onTick(long millisUntilFinished) {
        if(listener != null){
            listener.timeCountDown();
        }
    }
}