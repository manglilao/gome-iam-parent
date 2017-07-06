package com.gome.iam.domain.response;

/**
 * @author yintongjiang
 * @params
 * @since 2016/10/27
 */
public class VerifyRespData {
    private String userId;
    private String userName;
    private Long createTime;
    private Long expireTime;
    private String domain;

    public VerifyRespData() {

    }

    public VerifyRespData(String userId, String userName, Long expireTime, Long createTime,String domain) {
        this.userId = userId;
        this.userName = userName;
        this.expireTime = expireTime;
        this.createTime = createTime;
        this.domain = domain;
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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
