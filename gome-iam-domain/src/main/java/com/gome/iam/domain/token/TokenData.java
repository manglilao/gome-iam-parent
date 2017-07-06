package com.gome.iam.domain.token;

/**
 * @author yintongjiang
 * @params
 * @since 2016/10/21
 */
public class TokenData {
    private String userName;
    private String value;
    private Long createTime;
    private Long expireTime;

    public TokenData(String userName, String value, Long createTime, Long expireTime) {
        this.userName = userName;
        this.value = value;
        this.createTime = createTime;
        this.expireTime = expireTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }
}
