package com.gome.iam.dao.user;

import com.gome.iam.domain.user.UserErrorCount;

import java.util.List;

/**
 * Created by admin on 2017/6/23.
 */
public interface UserErrorCountMapper {

    //新增一条记录
    void addOneNode(UserErrorCount userErrorCount);

    //根据用户 uuid 查询信息
    List<UserErrorCount> selectOneNode(String uid);

    int selectErrorCount(UserErrorCount userErrorCount);

    void deleteBeforeRecord(String uid);

}
