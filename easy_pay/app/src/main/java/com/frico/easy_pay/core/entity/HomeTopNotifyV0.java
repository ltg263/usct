package com.frico.easy_pay.core.entity;

import java.io.Serializable;

public class HomeTopNotifyV0 implements Serializable {
    private String content;

    public String getNotify() {
        return content;
    }

    public void setNotify(String notify) {
        this.content = notify;
    }
}
