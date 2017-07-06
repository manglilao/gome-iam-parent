package com.gome.iam.service.impl.user;

import com.alibaba.fastjson.JSON;
import com.gome.iam.common.util.HttpRequestUtil;
import com.gome.iam.dao.user.LocalUserMapper;
import com.gome.iam.domain.app.Application;
import com.gome.iam.domain.constant.AppStatus;
import com.gome.iam.domain.constant.LocalUserStatus;
import com.gome.iam.domain.exception.BusinessException;
import com.gome.iam.domain.response.PageData;
import com.gome.iam.domain.user.LocalUser;
import com.gome.iam.domain.user.LocalUserVo;
import com.gome.iam.service.api.AppService;
import com.gome.iam.service.api.SSOService;
import com.gome.iam.service.user.LocalUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 本地用户
 *
 * @author yintongjiang
 * @params
 * @since 2016/11/7
 */
@Service
public class LocalUserServiceImpl implements LocalUserService {
    private final static Logger LOGGER = LoggerFactory.getLogger(LocalUserServiceImpl.class);
    @Autowired
    private LocalUserMapper localUserMapper;
    @Autowired
    private AppService appService;
    @Autowired
    private SSOService ssoService;
    @Value("#{config['fortress.Machine.Url']}")
    private String fMUrl;

    @Override
    public void save(LocalUser localUser) {
        //localUser.setPassword(Md5Util.encrypt(localUser.getPassword()));
        localUser.setPassword(localUser.getPassword());
        localUserMapper.save(localUser);
    }

    @Override
    public void update(LocalUser localUser) {
        localUserMapper.update(localUser);
    }

    @Override
    @Transactional
    public void resetPwd(LocalUser localUser) throws Exception {
        LocalUser user = localUserMapper.findById(localUser.getId());
        String userName = user.getUserName();
        String password = localUser.getPassword();
//        localUser.setPassword(Md5Util.encrypt(password));
        localUser.setPassword(password);
        localUserMapper.update(localUser);
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("userName", userName);
        mapParam.put("passwd", password);
        String resp = HttpRequestUtil.doPost(this.fMUrl, mapParam);
        LOGGER.info("resetPwd resp={}", resp);
        if ("".equals(resp) || "false".equals(JSON.parseObject(resp).getString("success"))) {
            throw new BusinessException("更新堡垒机用户密码失败");
        }
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        //先调用应用删除用户接口删除用户
        List<Application> applicationList = appService.queryByStatus(AppStatus.RUNNING.getStatus());
        LocalUser localUser = localUserMapper.findById(id);
        ssoService.delUser(applicationList, localUser);
        //再删除本地用户
        localUserMapper.deleteById(id);
    }

    @Override
    public LocalUser findByUserName(String userName) {
        return localUserMapper.findByUserName(userName);
    }

    @Override
    public LocalUser findById(Integer id) {
        return localUserMapper.findById(id);
    }

    @Override
    public LocalUser findUser(String userName, String password) {
        LocalUser localUser = localUserMapper.findByUserName(userName);
        if (null != localUser && null != password && !LocalUserStatus.STOPPING.getStatus().equals(localUser.getStatus())) {
            if (java.util.Objects.equals(password, localUser.getPassword())) {
                localUserMapper.updateLoginTime(localUser.getId());
                return localUserMapper.findById(localUser.getId());
            }
        }
        return null;
    }

    @Override
    public PageData<LocalUser> findByPage(LocalUserVo localUserVo) {

        List<LocalUser> localUserList = localUserMapper.findByPage(localUserVo);
        return new PageData<>(localUserList, findByCount(localUserVo.getUserName()));
    }

    @Override
    public int findByCount(String userName) {
        return localUserMapper.findByCount(userName);
    }

    @Override
    public List<LocalUser> findAllUser() {
        return null;
    }

    @Override
    public void updateStatusById(Integer id, Integer status) {
        localUserMapper.updateStatusById(id, status);
    }

    @Override
    public List<LocalUser> findLikeUser(String userName) {
        return localUserMapper.findLikeUser(userName);
    }

    @Override
    public boolean modifyLocalUserPassword(String userName, String old_password, String new_password) {
        int effect_count = localUserMapper.modifyLocalUserPassword(userName, old_password, new_password);
        return effect_count > 0;
    }

    @Override
    public LocalUser findByEmail(String email) {
        return localUserMapper.findByEmail(email);
    }

    @Override
    public int findUserByEmailOrPhone(LocalUser localUser) {
        return localUserMapper.findUserByEmailOrPhone(localUser);
    }

    @Override
    public Integer findUserIdByCondition(LocalUser localUser) {
        return localUserMapper.findUserIdByCondition(localUser);
    }

    @Override
    public LocalUser selectUserWithUsernameOrTelOrEmail(String userName) {
        return localUserMapper.selectUserWithUsernameOrTelOrEmail(userName);
    }

    @Override
    public void updateUserEmailStatus(String email) {
        localUserMapper.updateUserEmailStatus(email);
    }

    @Override
    public LocalUser selectUserWithUUID(String uuid) {
        return localUserMapper.selectUserWithUUID(uuid);
    }

    @Override
    public int selectRegisterUserCount(LocalDateTime createDate) {
        return localUserMapper.selectRegisterUserCount(createDate);
    }

    @Override
    public void updateLoginTime(Integer id) {
        localUserMapper.updateLoginTime(id);
    }

    @Override
    public void updateLockedEndTime(LocalUser localUser) {
        localUserMapper.updateLockedEndTime(localUser);
    }
}
