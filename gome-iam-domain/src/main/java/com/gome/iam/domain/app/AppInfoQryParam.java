package com.gome.iam.domain.app;

import java.io.Serializable;

/**
 * @author luoji
 * @params
 * @since 2016/10/25 0025
 */
public class AppInfoQryParam implements Serializable {

    private static final long serialVersionUID = -8580263247527524314L;
    private String appName;
    private Integer offset;
    private Integer limit;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
