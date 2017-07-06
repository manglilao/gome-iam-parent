package com.gome.iam.dao.user;

import com.gome.iam.domain.user.UserPwdHistory;

import java.util.List;

/**
 * Created by qiaowentao on 2017/6/16.
 */
public interface UserPwdHistoryMapper {

    void insert(UserPwdHistory userPwdHistory);

    void delete(UserPwdHistory userPwdHistory);

    List<UserPwdHistory> selectList(UserPwdHistory userPwdHistory);

}
