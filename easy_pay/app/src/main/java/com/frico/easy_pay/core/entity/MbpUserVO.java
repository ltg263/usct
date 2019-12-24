package com.frico.easy_pay.core.entity;


import android.text.TextUtils;

import java.io.Serializable;

public class MbpUserVO implements Serializable {

    /**
     * id : 84
     * regcode : 9EWJ3N
     * acqid : 600084
     * level : 1
     * username : wang
     * email : 476458025@qq.com
     * mobile : 17682329232
     * token : 26ce7e87-e194-4106-9cfd-2af177e90547
     * available_money : 982496.6239   可用余额sct
     * frozen_money : -13634.3339    冻结的额度sct
     * give_money : 0.0000
     * profit_money : 335.8623    收益
     * max : 972768.9345
     * exchangerate : 6.8
     */

    private String id;
    private String regcode;
    private String acqid;
    private String level;
    private String username;
    private String email;           //邮箱
    private String mobile;
    private String token;
    private String available_money; //可用余额
    private String frozen_money;
    private String give_money;
    private String total_give_money;
    private String profit_money;
    private String total_profit_money;
    private String max;
    private String exchangerate;
    private String leveltext;
    private String total_amount;
    private String isnewmessage;
    private String expiretime;
    private String createtime;


    // 0 为 为设置或未绑定  1为已设置已绑定
    private String is_setpassword;      //是否设置了登录密码
    private String is_setpaypassword;   //是否设置了交易密码
    private String is_setemail;         //是否设置了邮箱
    private String is_setpayway;        //是否绑定了收款方式
    private String is_setavater;
    private String is_setusername;

    private int give_setpassword;        //": 赠送2sct,
    private int give_setpaypassword;     //": 赠送2sct,
    private int give_setemail;           //": 赠送2sct,
    private int give_setpayway;          //": 赠送2sct
    private int give_setavater;         //赠送1个sct
    private int give_setusername;       //赠送1个sct

    private String paymentstatus;           //normal 开    hidden 关闭
    private String orderautostatus;           //normal 开    hidden 关闭

    private String avater;           //头像url

    private double acq_withdraw_rate;//提现费率
    private int acq_withdraw_nums;//今日剩余提现次数
    private int acq_withdraw_amount;//最低提现金额

    private String ip;//socket 的ip

    private int is_activate ;                   // 是否激活:0=未激活,1=已激活
    private int is_activate_money;              // 激活金额

    private String is_vip;


    public void setIsVip(String isVip) {
        this.is_vip = isVip;
    }

    public String getIsVip() {
        return is_vip;
    }

    @Override
    public String toString() {
        return "MbpUserVO{" +
                "id='" + id + '\'' +
                ", regcode='" + regcode + '\'' +
                ", acqid='" + acqid + '\'' +
                ", level='" + level + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", token='" + token + '\'' +
                ", available_money='" + available_money + '\'' +
                ", frozen_money='" + frozen_money + '\'' +
                ", give_money='" + give_money + '\'' +
                ", total_give_money='" + total_give_money + '\'' +
                ", profit_money='" + profit_money + '\'' +
                ", total_profit_money='" + total_profit_money + '\'' +
                ", max='" + max + '\'' +
                ", exchangerate='" + exchangerate + '\'' +
                ", leveltext='" + leveltext + '\'' +
                ", total_amount='" + total_amount + '\'' +
                ", isnewmessage='" + isnewmessage + '\'' +
                ", expiretime='" + expiretime + '\'' +
                ", createtime='" + createtime + '\'' +
                ", is_setpassword='" + is_setpassword + '\'' +
                ", is_setpaypassword='" + is_setpaypassword + '\'' +
                ", is_setemail='" + is_setemail + '\'' +
                ", is_setpayway='" + is_setpayway + '\'' +
                ", is_setavater='" + is_setavater + '\'' +
                ", is_setusername='" + is_setusername + '\'' +
                ", give_setpassword=" + give_setpassword +
                ", give_setpaypassword=" + give_setpaypassword +
                ", give_setemail=" + give_setemail +
                ", give_setpayway=" + give_setpayway +
                ", give_setavater=" + give_setavater +
                ", give_setusername=" + give_setusername +
                ", paymentstatus='" + paymentstatus + '\'' +
                ", orderautostatus='" + orderautostatus + '\'' +
                ", avater='" + avater + '\'' +
                ", acq_withdraw_rate=" + acq_withdraw_rate +
                ", acq_withdraw_nums=" + acq_withdraw_nums +
                ", acq_withdraw_amount=" + acq_withdraw_amount +
                ", ip='" + ip + '\'' +
                ", is_activate=" + is_activate +
                ", is_activate_money=" + is_activate_money +
                ", isVip='" + is_vip + '\'' +
                '}';
    }

    public String getExpiretime() {
        return expiretime;
    }

    public void setExpiretime(String expiretime) {
        this.expiretime = expiretime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegcode() {
        return regcode;
    }

    public void setRegcode(String regcode) {
        this.regcode = regcode;
    }

    public String getAcqid() {
        return acqid;
    }

    public void setAcqid(String acqid) {
        this.acqid = acqid;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAvailable_money() {
        return available_money;
    }

    public void setAvailable_money(String available_money) {
        this.available_money = available_money;
    }

    public String getFrozen_money() {
        return frozen_money;
    }

    public void setFrozen_money(String frozen_money) {
        this.frozen_money = frozen_money;
    }

    public String getGive_money() {
        return give_money;
    }

    public void setGive_money(String give_money) {
        this.give_money = give_money;
    }

    public String getProfit_money() {
        return profit_money;
    }

    public void setProfit_money(String profit_money) {
        this.profit_money = profit_money;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getExchangerate() {
        return exchangerate;
    }

    public void setExchangerate(String exchangerate) {
        this.exchangerate = exchangerate;
    }

    public String getLeveltext() {
        return leveltext;
    }

    public void setLeveltext(String leveltext) {
        this.leveltext = leveltext;
    }

    public String getTotal_give_money() {
        return total_give_money;
    }

    public void setTotal_give_money(String total_give_money) {
        this.total_give_money = total_give_money;
    }

    public String getTotal_profit_money() {
        return total_profit_money;
    }

    public void setTotal_profit_money(String total_profit_money) {
        this.total_profit_money = total_profit_money;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getIsnewmessage() {
        return isnewmessage;
    }

    public void setIsnewmessage(String isnewmessage) {
        this.isnewmessage = isnewmessage;
    }


    public boolean getIs_setpassword() {
        return TextUtils.equals(is_setpassword,"1");
    }

    public void setIs_setpassword(String is_setpassword) {
        this.is_setpassword = is_setpassword;
    }

    public boolean getIs_setpaypassword() {
        return TextUtils.equals(is_setpaypassword,"1");
    }

    public void setIs_setpaypassword(String is_setpaypassword) {
        this.is_setpaypassword = is_setpaypassword;
    }

    public boolean getIs_setemail() {
        return TextUtils.equals(is_setemail,"1");
    }

    public void setIs_setemail(String is_setemail) {
        this.is_setemail = is_setemail;
    }

    public boolean getIs_setpayway() {
        return TextUtils.equals(is_setpayway,"1");
    }

    public String getIs_setavater() {
        return is_setavater;
    }

    public void setIs_setavater(String is_setavater) {
        this.is_setavater = is_setavater;
    }

    public String getIs_setusername() {
        return is_setusername;
    }

    public void setIs_setusername(String is_setusername) {
        this.is_setusername = is_setusername;
    }

    public boolean getIs_setUserName(){
        return TextUtils.equals(is_setusername,"1");
    }
    public boolean getIs_setHeader(){
        return TextUtils.equals(is_setavater,"1");
    }

    public void setIs_setpayway(String is_setpayway) {
        this.is_setpayway = is_setpayway;
    }

    public int getGive_setpassword() {
        return give_setpassword;
    }

    public void setGive_setpassword(int give_setpassword) {
        this.give_setpassword = give_setpassword;
    }

    public int getGive_setpaypassword() {
        return give_setpaypassword;
    }

    public void setGive_setpaypassword(int give_setpaypassword) {
        this.give_setpaypassword = give_setpaypassword;
    }

    public int getGive_setemail() {
        return give_setemail;
    }

    public void setGive_setemail(int give_setemail) {
        this.give_setemail = give_setemail;
    }

    public int getGive_setpayway() {
        return give_setpayway;
    }

    public void setGive_setpayway(int give_setpayway) {
        this.give_setpayway = give_setpayway;
    }

    public String getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }

    public boolean isPayWayOpen(){
        return TextUtils.equals(getPaymentstatus(),"normal");
    }

    public boolean isOrderAutoOpen(){
        return TextUtils.equals(getOrderautostatus(),"normal");
    }

    public String getmHeaderImgUrl() {
        return avater;
    }

    public void setmHeaderImgUrl(String mHeaderImgUrl) {
        this.avater = mHeaderImgUrl;
    }

    public int getGive_setavater() {
        return give_setavater;
    }

    public void setGive_setavater(int give_setavater) {
        this.give_setavater = give_setavater;
    }

    public int getGive_setusername() {
        return give_setusername;
    }

    public void setGive_setusername(int give_setusername) {
        this.give_setusername = give_setusername;
    }

    public String getAvater() {
        return avater;
    }

    public void setAvater(String avater) {
        this.avater = avater;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public boolean isPaymentStatusOpen(){
        return TextUtils.equals(getPaymentstatus(),"normal");
    }

    public boolean isOrderAutoStatusOpen(){
        return TextUtils.equals(getOrderautostatus(),"normal");
    }

    public double getRate() {
        return acq_withdraw_rate;
    }

    public void setRate(double rate) {
        this.acq_withdraw_rate = rate;
    }

    public int getTodayWithdrawCount() {
        return acq_withdraw_nums;
    }

    public void setTodayWithdrawCount(int todayWithdrawCount) {
        this.acq_withdraw_nums = todayWithdrawCount;
    }

    public int getAcq_withdraw_amount() {
        return acq_withdraw_amount;
    }

    public void setAcq_withdraw_amount(int acq_withdraw_amount) {
        this.acq_withdraw_amount = acq_withdraw_amount;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOrderautostatus() {
        return orderautostatus;
    }

    public void setOrderautostatus(String orderautostatus) {
        this.orderautostatus = orderautostatus;
    }

    public double getAcq_withdraw_rate() {
        return acq_withdraw_rate;
    }

    public void setAcq_withdraw_rate(double acq_withdraw_rate) {
        this.acq_withdraw_rate = acq_withdraw_rate;
    }

    public int getAcq_withdraw_nums() {
        return acq_withdraw_nums;
    }

    public void setAcq_withdraw_nums(int acq_withdraw_nums) {
        this.acq_withdraw_nums = acq_withdraw_nums;
    }

    /**
     * 是否激活了
     * @return
     */
    public boolean isActivated(){
        return getIs_activate() == 1;
    }

    public int getIs_activate() {
        return is_activate;
    }

    public void setIs_activate(int is_activate) {
        this.is_activate = is_activate;
    }

    public int getIs_activate_money() {
        return is_activate_money;
    }

    public void setIs_activate_money(int is_activate_money) {
        this.is_activate_money = is_activate_money;
    }
}