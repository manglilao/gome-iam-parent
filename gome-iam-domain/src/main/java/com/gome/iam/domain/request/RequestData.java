package com.gome.iam.domain.request;

import com.alibaba.fastjson.JSON;

/**
 * @author yintongjiang
 * @params
 * @since 2016/10/24
 */
public class RequestData {
    private String token;

    private RequestData(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static String toJSONString(String sign) {
        return JSON.toJSONString(new RequestData(sign));
    }
}
