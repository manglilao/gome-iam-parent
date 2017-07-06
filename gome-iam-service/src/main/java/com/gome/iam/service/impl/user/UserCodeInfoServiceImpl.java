package com.gome.iam.service.impl.user;

import com.gome.iam.dao.user.UserCodeInfoMapper;
import com.gome.iam.domain.user.UserCodeInfo;
import com.gome.iam.service.user.UserCodeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by qiaowentao on 2017/6/16.
 */
@Service
@Transactional(rollbackFor = Exception.class,isolation = Isolation.DEFAULT)
public class UserCodeInfoServiceImpl implements UserCodeInfoService {

    @Autowired
    private UserCodeInfoMapper userCodeInfoMapper;

    @Override
    @Transactional(isolation = Isolation.DEFAULT)
    public void insert(UserCodeInfo userCodeInfo) {
        userCodeInfoMapper.insert(userCodeInfo);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT)
    public void delete(UserCodeInfo userCodeInfo) {
        userCodeInfoMapper.delete(userCodeInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserCodeInfo> selectList(UserCodeInfo userCodeInfo) {
        return userCodeInfoMapper.selectList(userCodeInfo);
    }
}
