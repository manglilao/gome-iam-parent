package com.gome.iam.dao.user;

import com.gome.iam.domain.user.LocalUser;
import com.gome.iam.domain.user.LocalUserVo;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yintongjiang
 * @params
 * @since 2016/11/7
 */
public interface LocalUserMapper {
    void save(LocalUser localUser);

    void update(LocalUser localUser);

    void deleteById(Integer id);

    LocalUser findByUserName(String userName);

    LocalUser findById(Integer id);

//    List<LocalUser> findByPage(@Param(value = "userName") String userName, @Param(value = "offset") int offset, @Param(value = "limit") int limit);

    List<LocalUser> findByPage(LocalUserVo localUserVo);

    int findByCount(@Param(value = "userName") String userName);

    void updateLoginTime(Integer id);

    List<LocalUser> findAllUser();

    void updateStatusById(@Param(value = "id") Integer id, @Param(value = "status") Integer status);

    List<LocalUser> findLikeUser(String userName);

    int modifyLocalUserPassword(String userName, String old_password, String new_password);

    LocalUser findByEmail(String email);

    int findUserByEmailOrPhone(LocalUser localUser);

    Integer findUserIdByCondition(LocalUser localUser);

    LocalUser selectUserWithUsernameOrTelOrEmail(String userName);

    void updateUserEmailStatus(String email);

    LocalUser selectUserWithUUID(String uuid);

    int selectRegisterUserCount(LocalDateTime createDate);

    //更新用户账号冻结截止时间
    void updateLockedEndTime(LocalUser localUser);

}
