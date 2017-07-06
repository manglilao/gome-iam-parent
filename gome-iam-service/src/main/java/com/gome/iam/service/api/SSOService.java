package com.gome.iam.service.api;

import com.gome.iam.domain.app.Application;
import com.gome.iam.domain.response.RespData;
import com.gome.iam.domain.user.LocalUser;

import java.util.List;

/**
 * @author yintongjiang
 * @params
 * @since 2016/10/25
 */
public interface SSOService {
    RespData ssoLogout(String token);

    RespData getLoginUserInfo(String token);

    /**
     * 删除本地用户时候调用各个应用删除用户信息接口
     *
     * @author yintongjiang
     * @since 2016/11/21
     */
    RespData delUser(List<Application> applicationList, LocalUser localUser);


    /**
     * 验证用户是否在sso中注册
     *
     * @author gaoyanlei
     * @since 2016/11/18
     */
    public Object verifyUser(String userName);
}
