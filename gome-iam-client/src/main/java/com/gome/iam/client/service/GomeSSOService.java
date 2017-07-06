package com.gome.iam.client.service;

import javax.servlet.http.HttpServletResponse;


/**
 * @author yintongjiang
 * @params
 * @since 2016/10/24
 */
public interface GomeSSOService {
    void init();


    void removeToken(HttpServletResponse httpServletResponse, String token);


    void removeToken(String token);


    String getAppHomeUrl();


    String getAppKey();


    String getSsoLogOutUrl();
}
