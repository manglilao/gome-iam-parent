package com.gome.iam.domain.user;

import java.time.LocalDateTime;

/**
 * Created by qiaowentao on 2017/6/16.
 */
public class UserPwdHistory {

    private Integer id;

    //对应注册用户的id主键
    private Integer uid;

    private String password;

    private LocalDateTime operateTime;

    public UserPwdHistory() {
    }

    public UserPwdHistory(Integer id, Integer uid, String password, LocalDateTime operateTime) {
        this.id = id;
        this.uid = uid;
        this.password = password;
        this.operateTime = operateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(LocalDateTime operateTime) {
        this.operateTime = operateTime;
    }
}
