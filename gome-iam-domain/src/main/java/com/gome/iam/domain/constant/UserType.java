package com.gome.iam.domain.constant;

import java.io.Serializable;

/**
 * @author yintongjiang
 * @params
 * @since 2016/10/25
 */
public enum UserType implements Serializable {
    SYS_USER("系统用户"),
    LDAP_USER("LDAP用户"),
    LOCAL_USER("本地用户"),
    GOMEPLUS_USER("互联网用户");
    private String value;

    UserType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
