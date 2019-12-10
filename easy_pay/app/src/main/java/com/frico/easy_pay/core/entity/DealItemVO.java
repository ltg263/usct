package com.frico.easy_pay.core.entity;

/**
 * 创建日期：2019/7/22 on 17:59
 * 作者:王红闯 hongchuangwang
 */
public class DealItemVO {

    /**
     * id : 245
     * advertno : S201907191949093922
     * amount : 487.2000
     * frozenamount : 12.8000
     * tradeway : 1,2
     * tradeamount : 0.0000
     * amountmin : 1.0000
     * amountmax : 2.0000
     * status : 1
     * createtime : 2019-07-19 19:49:09
     * tradeway_text : 银行卡/支付宝
     */

    private int id;
    private String advertno;
    private String amount;
    private String frozenamount;
    private String tradeway;
    private String tradeamount;
    private String amountmin;
    private String amountmax;
    private String status;
    private String createtime;
    private String tradeway_text;
    private int tradetype;  //广告类型:1=会员卖出,2=商户卖出',
    private int rate;// 商户的广告订单赠送   例如：2   就是 2‰

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdvertno() {
        return advertno;
    }

    public void setAdvertno(String advertno) {
        this.advertno = advertno;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFrozenamount() {
        return frozenamount;
    }

    public void setFrozenamount(String frozenamount) {
        this.frozenamount = frozenamount;
    }

    public String getTradeway() {
        return tradeway;
    }

    public void setTradeway(String tradeway) {
        this.tradeway = tradeway;
    }

    public String getTradeamount() {
        return tradeamount;
    }

    public void setTradeamount(String tradeamount) {
        this.tradeamount = tradeamount;
    }

    public String getAmountmin() {
        return amountmin;
    }

    public void setAmountmin(String amountmin) {
        this.amountmin = amountmin;
    }

    public String getAmountmax() {
        return amountmax;
    }

    public void setAmountmax(String amountmax) {
        this.amountmax = amountmax;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getTradeway_text() {
        return tradeway_text;
    }

    public void setTradeway_text(String tradeway_text) {
        this.tradeway_text = tradeway_text;
    }

    public int getTradetype() {
        return tradetype;
    }

    public void setTradetype(int tradetype) {
        this.tradetype = tradetype;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
