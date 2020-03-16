package com.frico.usct.service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.frico.usct.R;
import com.frico.usct.config.Constant;
import com.frico.usct.core.api.RetrofitUtil;
import com.frico.usct.core.entity.NotificationCacheItemVO;
import com.frico.usct.core.entity.Result;
import com.frico.usct.core.netty.NettyConnector;
import com.frico.usct.core.netty.NettyHandler;
import com.frico.usct.core.utils.Constants;
import com.frico.usct.receiver.NetChangeListener;
import com.frico.usct.receiver.NetWorkChangeBroadcastReceiver;
import com.frico.usct.ui.activity.MainActivity;
import com.frico.usct.utils.LogUtils;
import com.frico.usct.utils.Prefer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class HelperNotificationListenerService extends NotificationListenerService implements NetChangeListener {

    private static final String TAG = "通知监听服务";
    private final static int FOREGROUND_ID = 1001;


    private PowerManager.WakeLock mWakeLock;
    private WifiManager.WifiLock mWifiLock;

    //当前要保存的msglist
    private volatile ArrayList<NotificationCacheItemVO> mMsgList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.e(TAG + "--通知服务--onCreate-");
        acquireWakeLock(this);

        //改成线上地址 域名+端口 服务器会实现负载均衡
        if (!TextUtils.isEmpty(Prefer.getInstance().getToken())) {
            /**
             * 长连接地址
             */
            LogUtils.e(TAG + "---常量-----" + Constants.APP_HOST + "----" + Constants.APP_PORT);
            NettyConnector.addHostPort();
//            NettyConnector.addHostPort(Constants.BASE_URL, Constants.APP_PORT);
            NettyConnector.connect(NettyHandler.getInstance()).connect(true);
        }

        //注册网络监听器
        NetWorkChangeBroadcastReceiver.getInstance(this).setOnNetWorkChangeListener(this);

        toggleNotificationListenerService();

    }

    /**
     * 解决杀进程后，再次进去无法收到通知的bug
     */
    private void toggleNotificationListenerService() {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(this, HelperNotificationListenerService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(this, HelperNotificationListenerService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onListenerConnected() {
        LogUtils.e(TAG + "--通知服务onListenerConnected---");
        StatusBarNotification[] notificationslist = getActiveNotifications();
        for (int i = 0; i < notificationslist.length; i++) {
            StatusBarNotification temp = notificationslist[i];
            LogUtils.e(TAG + "--通知服务 --当前通知栏消息-第" + i + "条 ：" + temp);
        }
        sendNotification();
    }

    @Override
    public void onListenerDisconnected() {
        LogUtils.e(TAG + "--通知服务-onListenerDisconnected--");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();
        String pkg = sbn.getPackageName();
        if (notification == null) {
            return;
        }

        Bundle extras = notification.extras;
        if (extras == null)
            return;

        LogUtils.e(TAG, "******获取到通知****");
        final String title = getNotificationTitle(extras);
        final String date = getNotificationTime(notification);
        final String content = getNotificationContent(extras) + date;//后面加上时间

        printNotify(date, title, content);

        if (Constant.LISTENING_TARGET_WX_PKG.equals(pkg)) {
//            if (content.contains("收款") && content.contains("元")) {
//                postMoney(3, content);
//            }else if(content.contains("ABCD")){
            postMoney(3, content, date);
//            }
        } else if (Constant.LISTENING_TARGET_ALI_PKG.equals(pkg)) {
//            if (content.contains("收款") && content.contains("元")) {
//                postMoney(2, content);
//            }else if(content.contains("ABCD")){
            postMoney(2, content, date);
//            }
        } else if (Constant.LISTENING_TARGET_MMS_PKG.equals(pkg)) { //短信通知
//            if (content.contains("余额") || content.contains("银行")) {
//                postMoney(1, content);
//            }else if(content.contains("ABCD")){
            postMoney(1, content, date);
//            }
        } else if (Constant.LISTENING_TARGET_YSF_PKG.equals(pkg)) {
            postMoney(4, content, date);
        }
        LogUtils.e(TAG, "*****处理通知消息结束**");
    }

    /**
     * 接收到消息传递到后台
     *
     * @param type
     * @param content
     */
    private void postMoney(int type, final String content, final String time) {
        if (TextUtils.isEmpty(content)) {
            LogUtils.e(TAG + "--通知服务-接收到消息为空，无需发送--");
            return;
        }
        LogUtils.e(TAG + "--通知服务-接收到消息传递到后台--" + content);

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
                            LogUtils.e(TAG + "--发送通知成功--");
                            //发送成功了就唤醒 发送一次缓存
                            LogUtils.e(TAG + "--成功发送了一条 激活发送通知消息--");
                            sendCacheToService();
                        } else {
                            LogUtils.e(TAG + "--发送通知失败--" + result.getMsg());
                            addSendFaildMsgg(type, content, time);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(TAG + "--发送通知失败22--" + e.getMessage());
                        addSendFaildMsgg(type, content, time);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        LogUtils.e(TAG, "-通知服务---onNotificationRemoved---");
    }

    /**
     * 获取通知消息time
     *
     * @param notification
     * @return
     */
    private String getNotificationTime(Notification notification) {
        long when = notification.when;
        Date date = new Date(when);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String time = format.format(date);
        return time;
    }

    /**
     * 获取通知消息title
     *
     * @return
     */
    private String getNotificationTitle(Bundle extras) {
        String title = null;
        title = extras.getString(Notification.EXTRA_TITLE, "");
        return title;
    }

    /**
     * 获取通知消息内容
     *
     * @return
     */
    private String getNotificationContent(Bundle extras) {
        String content = null;
        content = extras.getString(Notification.EXTRA_TEXT, "");
        return content;
    }

    /**
     * 获取通知消息内容
     *
     * @return
     */
    private String getNotificationContent(Notification notification) {
        Bundle extras = notification.extras;
        if (extras == null)
            return "";

        String content = null;
        content = extras.getString(Notification.EXTRA_TEXT, "");
        return content;
    }

    /**
     * 打印下
     *
     * @param time
     * @param title
     * @param content
     */
    private void printNotify(String time, String title, String content) {
        LogUtils.e(TAG, time);
        LogUtils.e(TAG, title);
        LogUtils.e(TAG, content);
    }


    private void sendNotification() {
        createNotificationChannel();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 10086, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);// FLAG_ONE_SHOT
        Notification notification = new NotificationCompat.Builder(this, "chat")
                .setContentTitle("易支付收单服务")
                .setContentText("正在为您收单，请不要结束应用~")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launch_image)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launch_image))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
//        manager.notify(1, notification); // 点击删除
        startForeground(FOREGROUND_ID, notification);// 点击不可删除
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "chat";
            String channelName = "聊天消息";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);

            channelId = "subscribe";
            channelName = "订阅消息";
            importance = NotificationManager.IMPORTANCE_DEFAULT;
            createNotificationChannel(channelId, channelName, importance);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    private Thread mThread;

    private void bgServiceWork() {
        if (mThread == null) {
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        Log.i(TAG, TAG + "USCT进程 t = " + System.currentTimeMillis());
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        if (!mThread.isAlive()) {
            mThread.start();
        }
    }

    private synchronized void addSendFaildMsgg(int type, String msgContent, String time) {
        mMsgList = getSpCacheMsgList();
        NotificationCacheItemVO item = new NotificationCacheItemVO();
        item.setNotificationContent(msgContent);
        item.setId(mMsgList.size());
        item.setType(type);
        item.setTime(time);

        //根据时间去重复的
        NotificationCacheItemVO tempitem;
        for (int i = 0; i < mMsgList.size(); i++) {
            tempitem = mMsgList.get(i);
            if (TextUtils.equals(tempitem.getTime(), time)) {
                return;
            }
        }
        mMsgList.add(item);
        LogUtils.e(TAG + "addSendFailedMsg缓存size = " + mMsgList.size() + "--发送通知到服务端失败---保存到缓存---" + msgContent);
        saveDataToSP(mMsgList);
    }


    private void saveDataToSP(ArrayList<NotificationCacheItemVO> msgList) {
        Gson gson = new Gson();
//        String personInfos = jsonArray.toString(); // 将JSONArray转换得到String
        String personInfos = gson.toJson(msgList); // 将JSONArray转换得到String
        LogUtils.e(TAG + "saveDataToSP缓存size = " + msgList.size() + "--保存所有数据到  SP---" + personInfos);
        Prefer.getInstance().saveNotificationMes(personInfos);
    }


    private ArrayList<NotificationCacheItemVO> getSpCacheMsgList() {
        String getNotList = Prefer.getInstance().getAllNotificationMsges();
        Gson gson = new Gson();
        ArrayList<NotificationCacheItemVO> list2 = new ArrayList<>();
        if (!TextUtils.isEmpty(getNotList)) {
            list2 = gson.fromJson(getNotList, new TypeToken<List<NotificationCacheItemVO>>() {
            }.getType());
        }
        return list2;
    }

    private void sendCacheToService() {

        NotificationCacheItemVO firstItem = null;
        ArrayList<NotificationCacheItemVO> spMsgList = mMsgList;
        if (spMsgList.size() > 0) {
            firstItem = spMsgList.get(0);
            postMoney(firstItem.getType(), firstItem.getNotificationContent(), firstItem.getTime());
            //发送出去了，就移除该对象
            mMsgList.remove(firstItem);
            //更新sp的存储
            saveDataToSP(mMsgList);
        } else {
            LogUtils.e(TAG + "--sendCacheToService 缓存消息发送完毕 ！！！！！--");
        }
    }


    @Override
    public void onChangeListener(NetworkInfo.State status) {
        if (status == NetworkInfo.State.CONNECTED) {
            LogUtils.e(TAG + "--onChangeListener 网络连接上了，激活发送通知消息--");
            LogUtils.e(TAG + "--sendCacheToService 激活发送通知消息--");
            sendCacheToService();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseWakeLock(this);
    }

    /**
     * 阻止系统休眠和wifi休眠
     *
     * @param ctx
     */
    @SuppressLint("InvalidWakeLockTag")
    private void acquireWakeLock(Context ctx) {
        if (mWakeLock == null) {
//            PowerManager pMgr = (PowerManager) ctx.getSystemService(POWER_SERVICE);    //保持当前进程持续有效
//                mWakeLock = pMgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
//                        "MywWakelock.");

            PowerManager pMgr = (PowerManager) getSystemService(Context.POWER_SERVICE);
            //保持cpu一直运行，不管屏幕是否黑屏
            if (pMgr != null) {
                mWakeLock = pMgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CPUKeepRunning");
            }

            WifiManager wMgr = (WifiManager) ctx.getSystemService(WIFI_SERVICE);       //保持wifi有效
            mWifiLock = wMgr.createWifiLock("SMS Backup");
        }
        mWakeLock.acquire();
        mWifiLock.acquire();
    }

    /**
     * 结束进程的时候，释放锁
     *
     * @param ctx
     */
    private void releaseWakeLock(Context ctx) {
        mWakeLock.release();
        mWifiLock.release();
    }

}
