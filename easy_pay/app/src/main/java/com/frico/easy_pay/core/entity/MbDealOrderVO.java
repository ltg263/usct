package com.frico.easy_pay.core.entity;

public class MbDealOrderVO {

    private MbDealOrderItemVO list;

    private String start;

    private String end;

    private String status;


    public MbDealOrderItemVO getList() {
        return list;
    }

    public void setList(MbDealOrderItemVO list) {
        this.list = list;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}