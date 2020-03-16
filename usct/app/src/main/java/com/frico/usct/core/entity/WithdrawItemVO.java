package com.frico.usct.core.entity;

/**
 *
 * 提现记录 数据item
 */

public class WithdrawItemVO {

    /**
     *                          "id":4,
     *                     "orderno":"20190927215005600006414",
     *                     "acqid":600006,
     *                     "money":"66.0000",
     *                     "realmoney":"66.0000",
     *                     "cnymoney":"448.8000",
     *                     "servicemoney":"0.0000",
     *                     "bankname":"工商银行",
     *                     "subbranch":"江南大道支行",
     *                     "accountname":"银行",
     *                     "cardnumber":"6226663358477856",
     *                     "status":"2",
     *                     "refusetime":null,
     *                     "refusereason":null,
     *                     "paytime":0,
     *                     "adminip":null,
     *                     "adminid":null,
     *                     "cancleip":null,
     *                     "cancletime":0,
     *                     "updatetime":"2019-09-27 21:50:05",
     *                     "createip":"115.205.0.102",
     *                     "createtime":"2019-09-27 21:50:05"
     */

            private String id;                      //":4,
            private String orderno;                 //":"20190927215005600006414",
            private String acqid;                   //":600006,
            private double money;                   //":"66.0000",
            private double realmoney;               //":"66.0000",
            private double cnymoney;                //":"448.8000",
            private double servicemoney;            //":"0.0000",
            private String bankname;                //":"工商银行",
            private String subbranch;               //":"江南大道支行",
            private String accountname;             //":"银行",
            private String cardnumber;              //":"6226663358477856",
            private int status;                     //":"2",
            private String refusetime;              //":null,
            private String refusereason;            //":null,
            private String paytime;                 //":0,
            private String adminip;                 //":null,
            private String adminid;                 //":null,
            private String cancleip;                //":null,
            private String cancletime;              //":0,
            private String updatetime;              //":"2019-09-27 21:50:05",
            private String createip;                //":"115.205.0.102",
            private String createtime;              //":"2019-09-27 21:50:05"


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getAcqid() {
        return acqid;
    }

    public void setAcqid(String acqid) {
        this.acqid = acqid;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getRealmoney() {
        return realmoney;
    }

    public void setRealmoney(double realmoney) {
        this.realmoney = realmoney;
    }

    public double getCnymoney() {
        return cnymoney;
    }

    public void setCnymoney(double cnymoney) {
        this.cnymoney = cnymoney;
    }

    public double getServicemoney() {
        return servicemoney;
    }

    public void setServicemoney(double servicemoney) {
        this.servicemoney = servicemoney;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getSubbranch() {
        return subbranch;
    }

    public void setSubbranch(String subbranch) {
        this.subbranch = subbranch;
    }

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRefusetime() {
        return refusetime;
    }

    public void setRefusetime(String refusetime) {
        this.refusetime = refusetime;
    }

    public String getRefusereason() {
        return refusereason;
    }

    public void setRefusereason(String refusereason) {
        this.refusereason = refusereason;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public String getAdminip() {
        return adminip;
    }

    public void setAdminip(String adminip) {
        this.adminip = adminip;
    }

    public String getAdminid() {
        return adminid;
    }

    public void setAdminid(String adminid) {
        this.adminid = adminid;
    }

    public String getCancleip() {
        return cancleip;
    }

    public void setCancleip(String cancleip) {
        this.cancleip = cancleip;
    }

    public String getCancletime() {
        return cancletime;
    }

    public void setCancletime(String cancletime) {
        this.cancletime = cancletime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getCreateip() {
        return createip;
    }

    public void setCreateip(String createip) {
        this.createip = createip;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
