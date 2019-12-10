package com.frico.easy_pay.core.entity;

import java.io.Serializable;

public class ChongBiInfoVO implements Serializable {

    private String address;             //": "这个是币地址AAAAAA",
    private String img;             //": "http://b-ssl.duitang.com/uploads/blog/201312/04/20131204184148_hhXUT.jpeg"

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
