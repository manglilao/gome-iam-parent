package com.gome.iam.domain.user;

/**
 * Created by qiaowentao on 2017/6/14.
 */
public class UserCode extends LocalUser {

    private String code;

    public UserCode() {
    }

    public UserCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
