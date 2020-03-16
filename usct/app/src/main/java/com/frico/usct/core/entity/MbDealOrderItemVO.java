package com.frico.usct.core.entity;

import java.util.List;

/**
 * 创建日期：2019/7/22 on 17:49
 * 作者:王红闯 hongchuangwang
 */
public class MbDealOrderItemVO {

    /**
     * total : 10
     * per_page : 10
     * current_page : 1
     * last_page : 1
     * data : [{"id":533,"tradetype":"1","advertno":"S20190719142745402","orderno":"201907221610194961","userid":600073,"amount":"100.00","payway":"0","status":"2","appealstatus":"1","paytime":null,"completetime":null,"cancletime":null,"createtime":"2019-07-22 16:10:19","type":"买入"}]
     */

    private int total;
    private int per_page;
    private int current_page;
    private int last_page;
    private List<DealOrderItemVO> data;

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

    public List<DealOrderItemVO> getData() {
        return data;
    }

    public void setData(List<DealOrderItemVO> data) {
        this.data = data;
    }
}
