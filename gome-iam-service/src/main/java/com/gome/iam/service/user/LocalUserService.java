package com.gome.iam.service.user;

import com.gome.iam.domain.response.PageData;
import com.gome.iam.domain.user.LocalUser;
import com.gome.iam.domain.user.LocalUserVo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yintongjiang
 * @params
 * @since 2016/11/7
 */
public interface LocalUserService {
    void save(LocalUser localUser);

    void update(LocalUser localUser);

    void resetPwd(LocalUser localUser) throws Exception;

    void deleteById(Integer id);

    LocalUser findByUserName(String userName);

    LocalUser findById(Integer id);

    LocalUser findUser(String userName, String password);

    PageData<LocalUser> findByPage(LocalUserVo localUserVo);

    int findByCount(String userName);

    List<LocalUser> findAllUser();

    void updateStatusById(Integer id, Integer status);

    List<LocalUser> findLikeUser(String userName);

//    RespData validatorImportLocalUser();

    boolean modifyLocalUserPassword(String userName, String old_password, String new_password);

    LocalUser findByEmail(String email);

    int findUserByEmailOrPhone(LocalUser localUser);

    Integer findUserIdByCondition(LocalUser localUser);

    LocalUser selectUserWithUsernameOrTelOrEmail(String userName);

    void updateUserEmailStatus(String email);

    LocalUser selectUserWithUUID(String uuid);

    int selectRegisterUserCount(LocalDateTime createDate);

    void updateLoginTime(Integer id);

    //更新用户账号冻结截止时间
    void updateLockedEndTime(LocalUser localUser);

}
