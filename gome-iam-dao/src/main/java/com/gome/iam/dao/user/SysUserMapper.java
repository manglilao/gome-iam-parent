package com.gome.iam.dao.user;

import com.gome.iam.domain.user.SysUser;
import org.springframework.stereotype.Repository;


/**
 * @author gaoyanlei
 * @since 2016/10/25
 */
@Repository
public interface SysUserMapper {

    SysUser findByUserNameAndPassword(SysUser sysUser);



}