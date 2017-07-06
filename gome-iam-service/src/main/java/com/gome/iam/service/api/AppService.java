package com.gome.iam.service.api;

import com.gome.iam.domain.app.Application;
import com.gome.iam.domain.response.RespData;

import java.util.List;


/**
 * @author yintongjiang
 * @params
 * @since 2016/10/24
 */
public interface AppService {


    List<Application> queryByStatus(Integer status);


    RespData verifyAppKeyAndToken(String appKey, String token);

    RespData verifyUrlAndToken(String redirectUrl, String token);


    RespData verifyAppKeyAndUrl(String appKey, String redirectUrl);


    RespData verifyUrl(String redirectUrl);
}
