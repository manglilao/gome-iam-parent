package com.gome.iam.domain.user;

/**
 * Created by qiaowentao on 2017/6/29.
 */
public class LucalUserUrl extends LocalUser {

    private String appKey;
    private String redirectUrl;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

}
