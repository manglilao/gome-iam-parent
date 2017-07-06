package com.gome.iam.service.user;

import com.gome.iam.domain.user.UserCodeInfo;

import java.util.List;

/**
 * Created by qiaowentao on 2017/6/16.
 */
public interface UserCodeInfoService {

    void insert(UserCodeInfo userCodeInfo);

    void delete(UserCodeInfo userCodeInfo);

    List<UserCodeInfo> selectList(UserCodeInfo userCodeInfo);

}
