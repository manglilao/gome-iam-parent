package com.gome.iam.domain.user;

import java.time.LocalDateTime;

/**
 * Created by qiaowentao on 2017/6/23.
 */
public class UserErrorCount {

    private Integer id;

    //对应 local_user 表中用户的uuid
    private String uid;

    //密码输错时间
    private LocalDateTime operateTime;

    public UserErrorCount() {
    }

    public UserErrorCount(Integer id, String uid, LocalDateTime operateTime) {
        this.id = id;
        this.uid = uid;
        this.operateTime = operateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public LocalDateTime getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(LocalDateTime operateTime) {
        this.operateTime = operateTime;
    }
}
