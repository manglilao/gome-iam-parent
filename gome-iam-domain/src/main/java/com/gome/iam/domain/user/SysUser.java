package com.gome.iam.domain.user;

import com.gome.iam.domain.base.BaseDomain;


/**
 * sys用户
 *
 * @author gaoyanlei
 * @since 2016/10/25
 */
public class SysUser extends BaseDomain {

    private static final long serialVersionUID = 1L;
    private Long sysUserId;
    private String userName;
    private String password;
    private String lastLoginDate;
    private String thisLoginDate;
    private int status;

    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getThisLoginDate() {
        return thisLoginDate;
    }

    public void setThisLoginDate(String thisLoginDate) {
        this.thisLoginDate = thisLoginDate;
    }

    public Long getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
