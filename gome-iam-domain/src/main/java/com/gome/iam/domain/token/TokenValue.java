package com.gome.iam.domain.token;

import com.gome.iam.domain.constant.UserType;

import java.io.Serializable;


/**
 * @author luoji
 * @params
 * @since 2016/10/25 0025
 */
public class TokenValue implements Serializable {

    private static final long serialVersionUID = 5630951756321467619L;
    private String appKey;
    private UserType userType;
    private String userId;
    private String userName;
    private Long createTime;
    private Long expireTime;
    private String domain;

    public TokenValue() {

    }

    public TokenValue(String appKey, UserType userType, String userId, String userName, Long expireTime, Long createTime,String domain) {
        this.appKey = appKey;
        this.userType = userType;
        this.userId = userId;
        this.userName = userName;
        this.expireTime = expireTime;
        this.createTime = createTime;
        this.domain = domain;
    }

    public String getAppKey() {
        return appKey;
    }


    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String toString() {
        return "TokenValue{" +
                "appKey='" + appKey + '\'' +
                ", userType=" + userType +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", createTime=" + createTime +
                ", expireTime=" + expireTime +
                ", domain=" + domain +
                '}';
    }
}
