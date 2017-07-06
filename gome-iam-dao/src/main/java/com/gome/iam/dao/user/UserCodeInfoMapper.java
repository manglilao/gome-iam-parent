package com.gome.iam.dao.user;

import com.gome.iam.domain.user.UserCodeInfo;

import java.util.List;

/**
 * Created by qiaowentao on 2017/6/15.
 */
public interface UserCodeInfoMapper {

    void insert(UserCodeInfo userCodeInfo);

    void delete(UserCodeInfo userCodeInfo);

    List<UserCodeInfo> selectList(UserCodeInfo userCodeInfo);

}
