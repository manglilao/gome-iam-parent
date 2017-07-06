package com.gome.iam.service.ldap;

import com.gome.iam.domain.user.SSOUser;

/**
 * Ldap实现接口
 *
 * @author gaoyanlei
 * @since 2016/10/28
 */


public interface LDAPService {
    /**
     * 提供sso整体接口验证用户
     *
     * @author gaoyanlei
     */
    public SSOUser verifyUser(String userName, String password);

    /**
     * ldap 验证接口
     *
     * @author gaoyanlei
     */
    public boolean checkLDAPUser(String userName, String password);

    /**
     * 按用户名查询
     *
     * @author gaoyanlei
     */
    public SSOUser findLdapUserInfo(String userName);

    public boolean isEffect();

    SSOUser verifyGomePlusUser(String userName, String password);

}
