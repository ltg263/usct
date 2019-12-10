package com.frico.easy_pay.core;

public class BasePayWayListItemBean {

    public static final int TYPE_BANK = 1;
    public static final int TYPE_CODE_ALIPAY = 2;
    public static final int TYPE_CODE_WEIXIN = 3;

    private int type;
    private String id;
    private String nickName;
    private String aliasName;
    private String currentSctCount;
    private String payWayStatus;
    private int verifyStatus;
    private String alipayPID;
    private int isdefault;//是否默认:0=否,1=是  只对二维码有效

     public int getType() {
         return type;
     }

     public void setType(int type) {
         this.type = type;
     }

     public String getId() {
         return id;
     }

     public void setId(String id) {
         this.id = id;
     }

     public String getNickName() {
         return nickName;
     }

     public void setNickName(String nickName) {
         this.nickName = nickName;
     }

     public String getAliasName() {
         return aliasName;
     }

     public void setAliasName(String aliasName) {
         this.aliasName = aliasName;
     }

     public String getCurrentSctCount() {
         return currentSctCount;
     }

     public void setCurrentSctCount(String currentSctCount) {
         this.currentSctCount = currentSctCount;
     }

     public String getPayWayStatus() {
         return payWayStatus;
     }

     public void setPayWayStatus(String payWayStatus) {
         this.payWayStatus = payWayStatus;
     }

     public int getVerifyStatus() {
         return verifyStatus;
     }

     public void setVerifyStatus(int verifyStatus) {
         this.verifyStatus = verifyStatus;
     }

     public String getAlipayPID() {
         return alipayPID;
     }

     public void setAlipayPID(String alipayPID) {
         this.alipayPID = alipayPID;
     }

     public boolean isDefault(){
         return getIsdefault() == 1;
     }

    public int getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(int isdefault) {
        this.isdefault = isdefault;
    }
}
