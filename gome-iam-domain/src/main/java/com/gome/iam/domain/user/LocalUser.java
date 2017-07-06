package com.gome.iam.domain.user;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author yintongjiang
 * @params
 * @since 2016/11/7
 */
public class LocalUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String uuid;
    private String userName;
    private String email;
    private String realName;
    private String password;
    private Integer sex = 1;
    private String tel;
    private String company;
    private Integer type = 2;
    private String post;
    private Date createDate;
    private Date updateDate;
    private Date thisLoginDate;
    private Date lastLoginDate;
    private Integer createUserId;
    private Integer updateUserId;
    private Integer status = 1;
    //邮箱状态 0：未绑定；1：绑定(用户注册时绑定邮箱)
    private Integer emailStatus = 0;

    //账号冻结截止时间
    private Date lockedEndTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getThisLoginDate() {
        return thisLoginDate;
    }

    public void setThisLoginDate(Date thisLoginDate) {
        this.thisLoginDate = thisLoginDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public Integer getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(Integer emailStatus) {
        this.emailStatus = emailStatus;
    }

    public Date getLockedEndTime() {
        return lockedEndTime;
    }

    public void setLockedEndTime(Date lockedEndTime) {
        this.lockedEndTime = lockedEndTime;
    }
}
