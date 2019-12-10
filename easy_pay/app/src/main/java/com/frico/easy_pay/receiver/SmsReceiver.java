package com.frico.easy_pay.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.frico.easy_pay.core.api.RetrofitUtil;
import com.frico.easy_pay.core.entity.Result;
import com.frico.easy_pay.utils.LogUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 创建日期：2019/7/25 on 11:16
 * 作者:王红闯 hongchuangwang
 */
public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (null == bundle)
                return;
            final Object[] objects = (Object[]) bundle.get("pdus");
            for (Object object : objects) {
                SmsMessage msg = SmsMessage.createFromPdu((byte[]) object);
                Date date = new Date(msg.getTimestampMillis());// 时间
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String receiveTime = format.format(date);
                String phoneNumber = msg.getOriginatingAddress();
                String body = msg.getDisplayMessageBody();

//                String need = "接收到短信来自：\n" + msg.getOriginatingAddress() + "\n内容：\n" + msg.getDisplayMessageBody() + "\n时间：\n+" + receiveTime;
                LogUtils.e("--接受到短信内容-", msg.getDisplayMessageBody());
                if (body.contains("余额") || body.contains("银行")) {
                    postMoney(1, body);
                }
            }
        } catch (Exception e) {
            LogUtils.e(getClass().getName(), e.getMessage());
        }
    }


    /**
     * 接收到消息传递到后台
     *
     * @param type
     * @param content
     */
    private void postMoney(int type, final String content) {
        LogUtils.e("--通知服务-接收到消息传递到后台--");

        RetrofitUtil.getInstance().apiService()
                .sendnotice(type, content)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        if (result.getCode() == 1) {
                            LogUtils.e("--发送通知成功--");
                        } else {
                            LogUtils.e("--发送通知失败--" + result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("--发送通知失败--" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
