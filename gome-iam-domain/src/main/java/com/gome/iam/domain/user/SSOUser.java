package com.gome.iam.domain.user;

import com.gome.iam.domain.base.BaseDomain;

import java.util.Date;


/**
 * sso用户
 *
 * @author gaoyanlei
 * @since 2016/10/25
 */
public class SSOUser extends BaseDomain {

    private static final long serialVersionUID = 1L;
    private Long ssoUserId;
    private String ldapUserId;
    private String userName;
    private String userTitle;
    private String organization;
    private String password;
    private String bindDn;
    private String base;
    private Date lastLoginDate;
    private Date thisLoginDate;
    private int status;
    // Add at 2016-12-30
    private int version;

    public SSOUser(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public SSOUser() {
    }

    public Long getSsoUserId() {
        return ssoUserId;
    }


    public void setSsoUserId(Long ssoUserId) {
        this.ssoUserId = ssoUserId;
    }


    public String getLdapUserId() {
        return ldapUserId;
    }

    public void setLdapUserId(String ldapUserId) {
        this.ldapUserId = ldapUserId;
    }

    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getOrganization() {
        return organization;
    }


    public void setOrganization(String organization) {
        this.organization = organization;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getBindDn() {
        return bindDn;
    }


    public void setBindDn(String bindDn) {
        this.bindDn = bindDn;
    }


    public String getBase() {
        return base;
    }


    public void setBase(String base) {
        this.base = base;
    }


    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public Date getThisLoginDate() {
        return thisLoginDate;
    }

    public void setThisLoginDate(Date thisLoginDate) {
        this.thisLoginDate = thisLoginDate;
    }


    public String getUserTitle() {
        return userTitle;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public int getStatus() {
        return status;
    }


    public void setStatus(int status) {
        this.status = status;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
