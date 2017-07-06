package com.gome.iam.service.impl.user;

import com.gome.iam.dao.user.SysUserMapper;
import com.gome.iam.domain.user.SysUser;
import com.gome.iam.service.user.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * sys用户
 *
 * @author gaoyanlei
 * @since 2016/10/25
 */

@Service
public class SysUserServiceImpl implements ISysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public SysUser findByUserNameAndPassword(SysUser sysUser) {
        return sysUserMapper.findByUserNameAndPassword(sysUser);
    }
}
