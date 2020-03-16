package com.frico.usct.ui.activity.response;

/**
 * 创建日期：2019/8/1 on 11:18
 * 作者:王红闯 hongchuangwang
 */
public class LoginVO {
    private String type;
    private String user_id;
    private String user_name;
    private String expiretime;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getExpiretime() {
        return expiretime;
    }

    public void setExpiretime(String expiretime) {
        this.expiretime = expiretime;
    }
}
