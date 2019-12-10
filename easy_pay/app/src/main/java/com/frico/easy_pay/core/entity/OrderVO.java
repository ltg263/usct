package com.frico.easy_pay.core.entity;

/**
 * 创建日期：2019/7/22 on 11:39
 * 作者:王红闯 hongchuangwang
 */

public class OrderVO {

    /**
     * id : 402
     * ordernum : pd3fbf34d2a7fd6e5d8c6-876
     * amount : 11.9000
     * paytype : 3
     * payamount : 0.0000
     * payaccount :
     * paytime : null
     * createtime : 2019-07-22 11:18:09
     * info : {"id":83,"accountname":"陈彬73","img":"http://pay.fricobloc.com/uploads/2019_07_17/5d2eff015f36d.png","alias":"73"}
     * status : 1
     * callbackstatus : 0
     * bank_or_code : 83
     * acq_profit_percentage : null
     * acqprofit : 0.0000
     * receivetime : null
     * difftime : 1112
     */

    private int id;
    private String ordernum;
    private String amount;
    private String paytype;
    private String payamount;
    private String payaccount;
    private String paytime;
    private String createtime;
    private PayWayVO info;
    private String status;
    private String callbackstatus;
    private String bank_or_code;
    private String acq_profit_percentage;
    private String acqprofit;
    private String receivetime;
    private int difftime;
    private String cnymoney;
    private String realpaymoney;//realpaymoney
    private int iscancancle; //   1 可以取消 0 不可以取消
    private long autocancletime;//订单超时时间  单位s
    private long createTimeLong;//订单创建时间
    private long residueTimeLong;//剩余倒计时时间
    private int canclereson;//  2: 为补单，不要轮询接单列表

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getPayamount() {
        return payamount;
    }

    public void setPayamount(String payamount) {
        this.payamount = payamount;
    }

    public String getPayaccount() {
        return payaccount;
    }

    public void setPayaccount(String payaccount) {
        this.payaccount = payaccount;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public PayWayVO getInfo() {
        return info;
    }

    public void setInfo(PayWayVO info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCallbackstatus() {
        return callbackstatus;
    }

    public void setCallbackstatus(String callbackstatus) {
        this.callbackstatus = callbackstatus;
    }

    public String getBank_or_code() {
        return bank_or_code;
    }

    public void setBank_or_code(String bank_or_code) {
        this.bank_or_code = bank_or_code;
    }

    public String getAcq_profit_percentage() {
        return acq_profit_percentage;
    }

    public void setAcq_profit_percentage(String acq_profit_percentage) {
        this.acq_profit_percentage = acq_profit_percentage;
    }

    public String getAcqprofit() {
        return acqprofit;
    }

    public void setAcqprofit(String acqprofit) {
        this.acqprofit = acqprofit;
    }

    public String getReceivetime() {
        return receivetime;
    }

    public void setReceivetime(String receivetime) {
        this.receivetime = receivetime;
    }

    public int getDifftime() {
        return difftime;
    }

    public void setDifftime(int difftime) {
        this.difftime = difftime;
    }

    public String getCnymoney() {
        return cnymoney;
    }

    public void setCnymoney(String cnymoney) {
        this.cnymoney = cnymoney;
    }

    public String getRealpaymoney() {
        return realpaymoney;
    }

    public void setRealpaymoney(String realpaymoney) {
        this.realpaymoney = realpaymoney;
    }

    public int getIscancancle() {
        return iscancancle;
    }

    public void setIscancancle(int iscancancle) {
        this.iscancancle = iscancancle;
    }

    public long getAutocancletime() {
        return autocancletime;
    }

    public void setAutocancletime(long autocancletime) {
        this.autocancletime = autocancletime;
    }

    public long getCreateTimeLong() {
        return createTimeLong;
    }

    public void setCreateTimeLong(long createTimeLong) {
        this.createTimeLong = createTimeLong;
    }

    public long getResidueTimeLong() {
        return residueTimeLong;
    }

    public void setResidueTimeLong(long residueTimeLong) {
        this.residueTimeLong = residueTimeLong;
    }

    public int getCanclereson() {
        return canclereson;
    }

    public void setCanclereson(int canclereson) {
        this.canclereson = canclereson;
    }
}
