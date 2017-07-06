package com.gome.iam.dao.user;

import org.springframework.stereotype.Repository;

import com.gome.iam.domain.user.SSOUser;


/**
 * ssoUser dao
 *
 * @author gaoyanlei
 * @since 2016/10/25
 */
@Repository
public interface SSOUserMapper {

    void save(SSOUser ssoUser);

    SSOUser findByUserName(String userName);

    int update(SSOUser ssoUser);

    Integer findMaxVersionByUserName(String userName);

    Long findSSOUserIdByUserName(String userName);
}