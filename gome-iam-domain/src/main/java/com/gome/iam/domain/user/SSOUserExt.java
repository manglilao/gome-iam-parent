package com.gome.iam.domain.user;

/**
 * @author yintongjiang
 * @params
 * @since 2016/10/31
 */
public class SSOUserExt extends SSOUser {
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
