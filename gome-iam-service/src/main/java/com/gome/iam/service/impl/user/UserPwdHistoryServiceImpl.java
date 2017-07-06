package com.gome.iam.service.impl.user;

import com.gome.iam.dao.user.UserPwdHistoryMapper;
import com.gome.iam.domain.user.UserPwdHistory;
import com.gome.iam.service.user.UserPwdHistoryService;
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
public class UserPwdHistoryServiceImpl implements UserPwdHistoryService {

    @Autowired
    private UserPwdHistoryMapper userPwdHistoryMapper;

    @Override
    public void insert(UserPwdHistory userPwdHistory) {
        userPwdHistoryMapper.insert(userPwdHistory);
    }

    @Override
    public void delete(UserPwdHistory userPwdHistory) {
        userPwdHistoryMapper.delete(userPwdHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserPwdHistory> selectList(UserPwdHistory userPwdHistory) {
        return userPwdHistoryMapper.selectList(userPwdHistory);
    }
}
