package com.gome.iam.service.user;

import com.gome.iam.domain.user.SSOUser;


/**
 * sso用户
 *
 * @author gaoyanlei
 * @since 2016/10/25
 */
public interface ISSOUserService {

    Long save(SSOUser ssoUser);

    int update(SSOUser ssoUser);

    SSOUser findByUserName(String userName);

}
