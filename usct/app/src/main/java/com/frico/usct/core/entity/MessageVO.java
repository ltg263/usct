package com.frico.usct.core.entity;

import java.util.List;

/**
 * 创建日期：2019/7/25 on 13:42
 * 作者:王红闯 hongchuangwang
 */
public class MessageVO {


    /**
     * total : 9
     * per_page : 15
     * current_page : 1
     * last_page : 1
     * data : [{"id":530,"acqid":600084,"title":"登录通知","content":"您于60.177.96.28登录成功","status":"0","updatetime":"2019-07-25 13:40:13","createtime":"2019-07-25 13:40:13"},{"id":508,"acqid":600084,"title":"登录通知","content":"您于60.177.96.28登录成功","status":"0","updatetime":"2019-07-25 12:34:33","createtime":"2019-07-25 12:34:33"},{"id":479,"acqid":600084,"title":"登录通知","content":"您于60.177.96.28登录成功","status":"0","updatetime":"2019-07-25 11:34:26","createtime":"2019-07-25 11:34:26"},{"id":450,"acqid":600084,"title":"登录通知","content":"您于60.177.96.28登录成功","status":"0","updatetime":"2019-07-25 09:54:53","createtime":"2019-07-25 09:54:53"},{"id":429,"acqid":600084,"title":"登录通知","content":"您于60.177.96.28登录成功","status":"0","updatetime":"2019-07-25 09:15:07","createtime":"2019-07-25 09:15:07"},{"id":395,"acqid":600084,"title":"登录通知","content":"您于183.157.87.231登录成功","status":"0","updatetime":"2019-07-24 22:23:54","createtime":"2019-07-24 22:23:54"},{"id":392,"acqid":600084,"title":"登录通知","content":"您于127.0.0.1登录成功","status":"0","updatetime":"2019-07-24 22:22:04","createtime":"2019-07-24 22:22:04"},{"id":390,"acqid":600084,"title":"登录通知","content":"您于115.199.111.193登录成功","status":"0","updatetime":"2019-07-24 22:01:01","createtime":"2019-07-24 22:01:01"},{"id":347,"acqid":600084,"title":"登录通知","content":"您于115.199.111.193登录成功","status":"0","updatetime":"2019-07-24 20:30:57","createtime":"2019-07-24 20:30:57"}]
     */

    private int total;
    private int per_page;
    private int current_page;
    private int last_page;
    private List<MessageItemVO> data;

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

    public List<MessageItemVO> getData() {
        return data;
    }

    public void setData(List<MessageItemVO> data) {
        this.data = data;
    }
}
