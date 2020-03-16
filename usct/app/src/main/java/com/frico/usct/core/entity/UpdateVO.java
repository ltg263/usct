package com.frico.usct.core.entity;

/**
 * 创建日期：2019/7/25 on 15:57
 * 作者:王红闯 hongchuangwang
 */
public class UpdateVO {

    /**
     * versioncode : 1
     * content : 更新内容
     * downloadurl : https://www.fastadmin.net/download.html
     * enforce : 1
     */

    private int versioncode;
    private String content;
    private String downloadurl;
    private int enforce;
    private String newversion;

    public int getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(int versioncode) {
        this.versioncode = versioncode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDownloadurl() {
        return downloadurl;
    }

    public void setDownloadurl(String downloadurl) {
        this.downloadurl = downloadurl;
    }

    public int getEnforce() {
        return enforce;
    }

    public void setEnforce(int enforce) {
        this.enforce = enforce;
    }

    public String getNewversion() {
        return newversion;
    }

    public void setNewversion(String newversion) {
        this.newversion = newversion;
    }
}
