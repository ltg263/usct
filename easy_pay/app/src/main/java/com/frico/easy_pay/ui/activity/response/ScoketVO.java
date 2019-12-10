package com.frico.easy_pay.ui.activity.response;

/**
 * 创建日期：2019/8/1 on 14:38
 * 作者:王红闯 hongchuangwang
 */
public class ScoketVO {



    private String type;
    private String user_id;
    private String user_name;
    private String loginsuccess;
    private String time;
    private String from_client_id;
    private String from_client_name;
    private String to_client_id;
    private String content;
    private String msgtype;     //        '5'  => '您的卖币广告，有会员抢单创建订单',
                                //        '6'  => '您的卖币广告，有会员抢单，取消支付',
                                //        '7'  => '您的卖币广告，有会员抢单，订单已支付待确认',
                                //        '8'  => '您的卖币广告，有会员抢单，订单已确认',
                                //        '9'  => '您抢单成功，请及时付款',
                                //        '10' => '您的抢单已取消支付',
                                //        '11' => '您的抢单已付款，请等待确认',
                                //        '12' => '您的抢单已确认，请留意资产变化',
                                //        '13' => '交易中心有新的广告发布',
    private String client_id;


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

    public String getLoginsuccess() {
        return loginsuccess;
    }

    public void setLoginsuccess(String loginsuccess) {
        this.loginsuccess = loginsuccess;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFrom_client_id() {
        return from_client_id;
    }

    public void setFrom_client_id(String from_client_id) {
        this.from_client_id = from_client_id;
    }

    public String getFrom_client_name() {
        return from_client_name;
    }

    public void setFrom_client_name(String from_client_name) {
        this.from_client_name = from_client_name;
    }

    public String getTo_client_id() {
        return to_client_id;
    }

    public void setTo_client_id(String to_client_id) {
        this.to_client_id = to_client_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    @Override
    public String toString() {
        return "ScoketVO{" +
                "type='" + type + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", loginsuccess='" + loginsuccess + '\'' +
                ", time='" + time + '\'' +
                ", from_client_id='" + from_client_id + '\'' +
                ", from_client_name='" + from_client_name + '\'' +
                ", to_client_id='" + to_client_id + '\'' +
                ", content='" + content + '\'' +
                ", msgtype='" + msgtype + '\'' +
                ", client_id='" + client_id + '\'' +
                '}';
    }
}
