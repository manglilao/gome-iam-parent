package com.gome.iam.domain.user;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yintongjiang
 * @params
 * @since 2016/11/7
 */
public class LocalUserExt extends LocalUser {
    private String createName;
    private String updateName;

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }
}
