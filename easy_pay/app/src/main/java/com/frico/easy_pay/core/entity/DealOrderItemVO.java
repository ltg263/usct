package com.frico.easy_pay.core.entity;

/**
 * 创建日期：2019/7/22 on 17:50
 * 作者:王红闯 hongchuangwang
 */
public class DealOrderItemVO {

    /**
     * id : 533
     * tradetype : 1
     * advertno : S20190719142745402
     * orderno : 201907221610194961
     * userid : 600073
     * amount : 100.00
     * payway : 0
     * status : 2
     * appealstatus : 1
     * paytime : null
     * completetime : null
     * cancletime : null
     * createtime : 2019-07-22 16:10:19
     * type : 买入
     */

    private int id;
    private String tradetype;
    private String advertno;
    private String orderno;
    private int userid;  // 如果是 买入 ，那就是自己，如果是卖出 就是对方
    private String amount;
    private String payway;
    private String status;
    private String appealstatus;//申诉状态:0=未申诉,1=已申诉,2=申诉成功,3=撤销申诉 4-已完成  5-系统处理中
    private String byappealstatus;//卖方申诉状态:0=未申诉,1=已申诉,2=申诉成功,3=撤销申诉 4-已完成  5-系统处理中
    private Object paytime;
    private Object completetime;
    private Object cancletime;
    private String createtime;
    private String type;
    private String byId;
    private int buttontype;
    private String payaccount;
    private String cnymoney;
    private String byuserid;// 如果是 买入 ，那就是对方，如果是卖出 就是自己


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTradetype() {
        return tradetype;
    }

    public void setTradetype(String tradetype) {
        this.tradetype = tradetype;
    }

    public String getAdvertno() {
        return advertno;
    }

    public void setAdvertno(String advertno) {
        this.advertno = advertno;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayway() {
        return payway;
    }

    public void setPayway(String payway) {
        this.payway = payway;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppealstatus() {
        return appealstatus;
    }

    public void setAppealstatus(String appealstatus) {
        this.appealstatus = appealstatus;
    }

    public Object getPaytime() {
        return paytime;
    }

    public void setPaytime(Object paytime) {
        this.paytime = paytime;
    }

    public Object getCompletetime() {
        return completetime;
    }

    public void setCompletetime(Object completetime) {
        this.completetime = completetime;
    }

    public Object getCancletime() {
        return cancletime;
    }

    public void setCancletime(Object cancletime) {
        this.cancletime = cancletime;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getById() {
        return byId;
    }

    public void setById(String byId) {
        this.byId = byId;
    }

    public int getButtontype() {
        return buttontype;
    }

    public void setButtontype(int buttontype) {
        this.buttontype = buttontype;
    }

    public String getPayaccount() {
        return payaccount;
    }

    public void setPayaccount(String payaccount) {
        this.payaccount = payaccount;
    }

    public String getCnymoney() {
        return cnymoney;
    }

    public void setCnymoney(String cnymoney) {
        this.cnymoney = cnymoney;
    }

    public String getByappealstatus() {
        return byappealstatus;
    }

    public void setByappealstatus(String byappealstatus) {
        this.byappealstatus = byappealstatus;
    }

    public String getByuserid() {
        return byuserid;
    }

    public void setByuserid(String byuserid) {
        this.byuserid = byuserid;
    }
}
