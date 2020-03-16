package com.frico.usct.receiver;

import android.net.NetworkInfo;

public interface NetChangeListener {

    void onChangeListener(NetworkInfo.State status);
}
