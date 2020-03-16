package com.frico.usct.core.entity;

/**
 * 创建日期：2019/7/22 on 11:41
 * 作者:王红闯 hongchuangwang
 */
public class PayWayVO {


    /**
     * id : 83
     * accountname : 陈彬73
     * img : http://pay.fricobloc.com/uploads/2019_07_17/5d2eff015f36d.png
     * alias : 73
     */

    private int id;
    private String accountname;
    private String img;
    private String alias;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
