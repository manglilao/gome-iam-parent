package com.gome.iam.service.user;

import com.gome.iam.domain.user.SysUser;


/**
 * sso用户
 *
 * @author gaoyanlei
 * @since 2016/10/25
 */
public interface ISysUserService {

    SysUser findByUserNameAndPassword(SysUser sysUser);

}
