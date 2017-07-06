package com.gome.iam.domain.app;

import java.util.Date;

/**
 * @author yintongjiang
 * @params
 * @since 2016/10/24
 */
public class Application {
    private Integer appId;
    private String ip;
    private String domain;
    private String appName;
    private String appUrl;
    private String appKey;
    private String expiredApi;
    private String delUserApi;
    private String creator;
    private Date createDate;
    private Integer status;

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getExpiredApi() {
        return expiredApi;
    }

    public void setExpiredApi(String expiredApi) {
        this.expiredApi = expiredApi;
    }

    public String getDelUserApi() {
        return delUserApi;
    }

    public void setDelUserApi(String delUserApi) {
        this.delUserApi = delUserApi;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
