package com.frico.usct.core.entity;

import java.io.Serializable;

public class QrcodeVo implements Serializable {

    public String getReceiveqrcode() {
        return receiveqrcode;
    }

    public void setReceiveqrcode(String receiveqrcode) {
        this.receiveqrcode = receiveqrcode;
    }

    private String receiveqrcode;
}
