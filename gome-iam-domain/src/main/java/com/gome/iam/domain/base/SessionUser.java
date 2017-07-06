package com.gome.iam.domain.base;

/**
 * sessionUser
 *
 * @author gaoyanlei
 * @since 2016/10/28
 */
public class SessionUser {
    private Long ssoUserId;
    private String userName;

    public Long getSsoUserId() {
        return ssoUserId;
    }

    public void setSsoUserId(Long ssoUserId) {
        this.ssoUserId = ssoUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
