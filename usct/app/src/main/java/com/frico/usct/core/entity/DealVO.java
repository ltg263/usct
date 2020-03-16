package com.frico.usct.core.entity;

import java.util.List;

/**
 * 创建日期：2019/7/22 on 17:58
 * 作者:王红闯 hongchuangwang
 */
public class DealVO {

    /**
     * total : 4
     * per_page : 15
     * current_page : 1
     * last_page : 1
     * data : [{"id":245,"advertno":"S201907191949093922","amount":"487.2000","frozenamount":"12.8000","tradeway":"1,2","tradeamount":"0.0000","amountmin":"1.0000","amountmax":"2.0000","status":"1","createtime":"2019-07-19 19:49:09","tradeway_text":"银行卡/支付宝"},{"id":243,"advertno":"S201907191929145582","amount":"97.0000","frozenamount":"3.0000","tradeway":"1,2","tradeamount":"0.0000","amountmin":"1.0000","amountmax":"1.0000","status":"1","createtime":"2019-07-19 19:29:14","tradeway_text":"银行卡/支付宝"},{"id":240,"advertno":"S20190719153903586","amount":"100.0000","frozenamount":"0.0000","tradeway":"1","tradeamount":"0.0000","amountmin":"1.0000","amountmax":"2.0000","status":"1","createtime":"2019-07-19 15:39:03","tradeway_text":"银行卡"},{"id":239,"advertno":"S20190719142745402","amount":"8800.9900","frozenamount":"1100.0000","tradeway":"1,2,3","tradeamount":"0.0000","amountmin":"11.0000","amountmax":"111.0000","status":"1","createtime":"2019-07-19 14:27:45","tradeway_text":"银行卡/支付宝/微信"}]
     */

    private int total;
    private int per_page;
    private int current_page;
    private int last_page;
    private List<DealItemVO> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public List<DealItemVO> getData() {
        return data;
    }

    public void setData(List<DealItemVO> data) {
        this.data = data;
    }
}
