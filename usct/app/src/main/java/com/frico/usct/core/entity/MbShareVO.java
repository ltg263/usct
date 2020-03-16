package com.frico.usct.core.entity;

/**
 * 创建日期：2019/7/19 on 13:42
 * 作者:王红闯 hongchuangwang
 */
public class MbShareVO {

    /**
     * regcode : MCGXQF
     * url : http://pay.fricobloc.com/acq_register?invitecode=MCGXQF
     * shareimg : http://pay.fricobloc.com/shareimg/MCGXQF.png
     */

    private String regcode;
    private String url;
    private String shareimg;

    public String getRegcode() {
        return regcode;
    }

    public void setRegcode(String regcode) {
        this.regcode = regcode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShareimg() {
        return shareimg;
    }

    public void setShareimg(String shareimg) {
        this.shareimg = shareimg;
    }
}
