package com.frico.easy_pay.ui.activity.response;

/**
 * Created by whc on 2018/6/8.
 */

public enum PayType {
    ZHIFUBAO(1), WEIXIN(2), BANK(3), YUE(4), C(0);

    // 成员变量
    private int code;

    // 构造方法
    PayType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
