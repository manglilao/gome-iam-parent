package com.gome.iam.domain.user;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by qiaowentao on 2017/6/15.
 */
public class UserCodeInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private Integer id;

    private String ip;

    private String tel;

    private String email;

    private LocalDateTime createTime;

    public UserCodeInfo() {
    }

    public UserCodeInfo(Integer id, String ip, String tel, String email, LocalDateTime createTime) {
        this.id = id;
        this.ip = ip;
        this.tel = tel;
        this.email = email;
        this.createTime = createTime;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreateDate() {
        return createTime;
    }

    public void setCreateDate(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
