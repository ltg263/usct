package com.frico.usct.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 当前网络状态接收器
 */
public class NetWorkChangeBroadcastReceiver extends BroadcastReceiver {

    private Context context;
    private NoNetWorkNotice noNetWorkNotice;
    private List<NetChangeListener> listenerList = new ArrayList<>();
    private static NetWorkChangeBroadcastReceiver mInstance;



    public static NetWorkChangeBroadcastReceiver getInstance(Context context){
        if(mInstance == null){
            mInstance = new NetWorkChangeBroadcastReceiver(context);
        }
        return mInstance;
    }

    private NetWorkChangeBroadcastReceiver(Context context) {
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (noNetWorkNotice == null) noNetWorkNotice = NoNetWorkNotice.getInstance(context);
            NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
            for (NetworkInfo networkInfo : networkInfos) {
                NetworkInfo.State state = networkInfo.getState();
                if (NetworkInfo.State.CONNECTED == state) {
                    notifyNetWorkChanged(state);
                    if (null != noNetWorkNotice && noNetWorkNotice.isShowing()) {
                        noNetWorkNotice.cancel();
                    }
                    return;
                }
            }
            if (null != noNetWorkNotice && !noNetWorkNotice.isShowing()) {
                noNetWorkNotice.show();
                notifyNetWorkChanged(NetworkInfo.State.DISCONNECTED);
            }
        }
    }

    public void onDestroy() {
        if (null != noNetWorkNotice) {
            if (noNetWorkNotice.isShowing()) {
                noNetWorkNotice.cancel();
            }

            context = null;
            noNetWorkNotice = null;
        }
    }

    public void setOnNetWorkChangeListener(NetChangeListener listener){
        listenerList.add(listener);
    }

    private void notifyNetWorkChanged(NetworkInfo.State status){
        if(listenerList.size() > 0){
            for (int i = 0;i< listenerList.size();i++){
                listenerList.get(i).onChangeListener(status);
            }
        }
    }

}
