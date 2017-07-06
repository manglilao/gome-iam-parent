package com.gome.iam.service.impl.user;

import com.gome.iam.dao.user.UserErrorCountMapper;
import com.gome.iam.domain.user.UserErrorCount;
import com.gome.iam.service.user.UserErrorCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by qiaowentao on 2017/6/23.
 */
@Service
public class UserErrorCountServiceImpl implements UserErrorCountService {

    @Autowired
    private UserErrorCountMapper userErrorCountMapper;

    @Override
    public void addOneNode(UserErrorCount userErrorCount) {
        userErrorCountMapper.addOneNode(userErrorCount);
    }

    @Override
    public List<UserErrorCount> selectOneNode(String uid) {
        return userErrorCountMapper.selectOneNode(uid);
    }

    @Override
    public int selectErrorCount(UserErrorCount userErrorCount) {
        return userErrorCountMapper.selectErrorCount(userErrorCount);
    }

    @Override
    public void deleteBeforeRecord(String uid) {
        userErrorCountMapper.deleteBeforeRecord(uid);
    }


}
