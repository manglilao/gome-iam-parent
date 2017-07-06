package com.gome.iam.client.config;

public class GomeSSOServerConfig {
    private String tokenUrl;
    private String loginUrl;
    private String logoutUrl;
    private String appHomeUrl;
    private String appKey;
    private String[] excludeUrl;
    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public String getAppHomeUrl() {
        return appHomeUrl;
    }

    public void setAppHomeUrl(String appHomeUrl) {
        this.appHomeUrl = appHomeUrl;
    }

    public String getAppKey() {
        return appKey;
    }


    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String[] getExcludeUrl() {
        return excludeUrl;
    }

    public void setExcludeUrl(String[] excludeUrl) {
        this.excludeUrl = excludeUrl;
    }
    public boolean hasExcludeUrl(String eUrl){
        if(null!=this.excludeUrl && this.excludeUrl.length>0){
            for(String url:this.excludeUrl){
              if(url.equals(eUrl)){
                 return true;
              }
            }
        }
        return false;
    }
}