package com.frico.usct.core.entity;

public class MbAppVersionVO {

    /**
     * 平台 IOS ANDROID
     */
    private String platform;
    /**
     * 版本号
     */
    private Integer version;
    /**
     * 升级备注
     */
    private String remark;
    /**
     *
     * @return
     */
    private String url;

    /**
     * 是否强制更新 0否 1是
     * @return
     */
    private String isForceUpdate;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIsForceUpdate() {
        return isForceUpdate;
    }

    public void setIsForceUpdate(String isForceUpdate) {
        this.isForceUpdate = isForceUpdate;
    }
}