package com.frico.easy_pay.receiver;

import android.net.NetworkInfo;

public interface NetChangeListener {

    void onChangeListener(NetworkInfo.State status);
}
