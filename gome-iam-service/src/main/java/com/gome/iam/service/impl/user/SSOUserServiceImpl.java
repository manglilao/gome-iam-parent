package com.gome.iam.service.impl.user;

import com.gome.iam.dao.user.SSOUserMapper;
import com.gome.iam.domain.user.SSOUser;
import com.gome.iam.service.user.ISSOUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * sso用户实现
 *
 * @author gaoyanlei
 * @since 2016/10/25
 */

@Service
public class SSOUserServiceImpl implements ISSOUserService {

    private static Logger LOG = LoggerFactory.getLogger(SSOUserServiceImpl.class);

    @Autowired
    private SSOUserMapper ssoUserMapper;

    @Override
    public Long save(SSOUser ssoUser) {
        Integer max_version = ssoUserMapper.findMaxVersionByUserName(ssoUser.getUserName());
        Integer cur_version = (max_version == null ? 0 : max_version) + 1;
        ssoUser.setVersion(cur_version);
        try {
            ssoUserMapper.save(ssoUser);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return ssoUserMapper.findSSOUserIdByUserName(ssoUser.getUserName());
        }
        return ssoUser.getSsoUserId();
    }

    @Override
    public int update(SSOUser ssoUser) {
        return ssoUserMapper.update(ssoUser);
    }

    @Override
    public SSOUser findByUserName(String userName) {
        return ssoUserMapper.findByUserName(userName);
    }

}
