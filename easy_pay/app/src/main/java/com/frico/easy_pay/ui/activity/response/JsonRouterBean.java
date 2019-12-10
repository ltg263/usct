package com.frico.easy_pay.ui.activity.response;

/**
 * 解析路由Json后实体对象
 */

public class JsonRouterBean {

    private String router_key;
    private String router_value;

    public String getRouter_key() {
        return router_key;
    }

    public void setRouter_key(String router_key) {
        this.router_key = router_key;
    }

    public String getRouter_value() {
        return router_value;
    }

    public void setRouter_value(String router_value) {
        this.router_value = router_value;
    }
}
