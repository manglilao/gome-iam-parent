package com.gome.iam.domain.constant;

/**
 * @author yintongjiang
 * @params
 * @since 2016/10/25
 */
public enum LocalUserStatus {
    RUNNING(1, "启用"),
    STOPPING(0, "停用");
    private Integer status;
    private String des;

    LocalUserStatus(Integer status, String des) {
        this.status = status;
        this.des = des;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
