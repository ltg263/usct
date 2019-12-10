package com.frico.easy_pay.core.entity;

/**
 * 创建日期：2019/7/24 on 14:42
 * 作者:王红闯 hongchuangwang
 */
public class NoticeVO {

    /**
     * id : 1
     * title : sct数字银行上线公告
     * url : http://pay.fricobloc.com/noticedetail/1
     */

    private int id;
    private String title;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
