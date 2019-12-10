package com.frico.easy_pay.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.frico.easy_pay.R;
import com.frico.easy_pay.SctApp;
import com.frico.easy_pay.ui.activity.MainActivity;
import com.frico.easy_pay.utils.Prefer;

public class NotificationUtils extends ContextWrapper {
    public static final int LOGIN_OUT_NOTIFICATION_ID = 1008;

    private NotificationManager manager;
    public static final String id = "channel_1";
    public static final String name = "channel_name_1";
    private static NotificationUtils mInstance;

    public static NotificationUtils getInstance(){
        if(mInstance == null){
            mInstance = new NotificationUtils(SctApp.getInstance().getApplicationContext());
        }
        return mInstance;
    }

    public NotificationUtils(Context context) {
        super(context);
    }

    public void createNotificationChannel() {
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
            getManager().createNotificationChannel(channel);
        }
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public Notification.Builder getChannelNotification(String title, String content) {
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.my_ring_552857;
        Uri soundUri = Uri.parse(uri);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Notification.Builder(getApplicationContext(), id)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(R.mipmap.ic_launch_image)
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
//                    .setSound(soundUri)
                    .setContentIntent(getPendingIntent())
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }
        return null;
    }

    public NotificationCompat.Builder getNotification_25(String title, String content) {
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.my_ring_552857;
        Uri soundUri = Uri.parse(uri);
        return new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launch_image)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
//                .setSound(soundUri)
                .setContentIntent(getPendingIntent())
                .setVisibility(Notification.VISIBILITY_PUBLIC);
    }

    /**
     * 发送通知栏消息
     * @param msgId
     * @param title
     * @param content
     *
     */
    public void sendNotification(int msgId, String title, String content) {
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel();
            Notification notification = getChannelNotification(title, content).build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            getManager().notify(msgId, notification);
            //(msgTypeInt >= 3 && msgTypeInt <= 8) || msgTypeInt == 12

            if(((msgId >= 2 && msgId <= 8) || msgId == 12) && Prefer.getInstance().getRingSwitchIsOpen()){
                //只有交易信息才会提示
                try {
                    Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.my_ring_552857);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), uri);
                    r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            Notification notification = getNotification_25(title, content).build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            getManager().notify(msgId, notification);
        }

    }

    /**
     * 通知栏跳转
     * @return
     */
    private PendingIntent getPendingIntent(){
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        return pendingIntent;
    }

    public void cancelNotificationById(int notificationId){
        getManager().cancel(notificationId);
    }
}
