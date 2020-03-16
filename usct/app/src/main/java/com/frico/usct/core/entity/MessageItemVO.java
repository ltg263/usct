package com.frico.usct.core.entity;

import java.io.Serializable;

/**
 * 创建日期：2019/7/24 on 14:57
 * 作者:王红闯 hongchuangwang
 */
public class MessageItemVO implements Serializable {

    /**
     * id : 1
     * acqid : 600084
     * title : 1
     * content : 1
     * status : 0
     * updatetime : 1970-01-01 08:00:00
     * createtime : 1970-01-01 08:00:00
     */

    private int id;
    private String acqid;
    private String title;
    private String content;
    private String status;
    private String updatetime;
    private String createtime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAcqid() {
        return acqid;
    }

    public void setAcqid(String acqid) {
        this.acqid = acqid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
