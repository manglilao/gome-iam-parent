package com.gome.iam.service.impl.api;

import com.gome.iam.domain.app.Application;
import com.gome.iam.domain.constant.AppStatus;
import com.gome.iam.domain.constant.RespCode;
import com.gome.iam.domain.response.RespData;
import com.gome.iam.domain.token.TokenEntity;
import com.gome.iam.domain.token.TokenValue;
import com.gome.iam.domain.user.LocalUser;
import com.gome.iam.domain.user.SSOUser;
import com.gome.iam.service.api.AppService;
import com.gome.iam.service.api.SSOService;
import com.gome.iam.service.cache.CacheService;
import com.gome.iam.service.ldap.LDAPService;
import com.gome.iam.service.secret.SecretService;
import com.gome.iam.service.user.ISSOUserService;
import com.gome.iam.service.user.LocalUserService;
import com.gome.iam.task.SSODelUserTask;
import com.gome.iam.task.SSOLogoutTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;


/**
 * @author yintongjiang
 * @params
 * @since 2016/10/25
 */
@Service
public class SSOServiceImpl implements SSOService {
    private final static Logger LOGGER = LoggerFactory.getLogger(SSOServiceImpl.class);
    @Autowired
    private AppService appService;

    @Autowired
    private CacheService cacheService;
    @Autowired
    private ISSOUserService issoUserService;
    @Autowired
    private LDAPService ldapService;
    @Autowired
    private LocalUserService localUserService;
    @Autowired
    @Qualifier("blowfishSecretServiceImpl")
    private SecretService secretService;
    private ExecutorService executor = Executors.newCachedThreadPool();


    public RespData ssoLogout(String token) {

        List<Application> applicationList = appService.queryByStatus(AppStatus.RUNNING.getStatus());
        if (null != applicationList && !applicationList.isEmpty()) {
            for (Application application : applicationList) {
                if (null != application.getExpiredApi()) {
                    SSOLogoutTask task = new SSOLogoutTask(application.getExpiredApi(), token);
                    FutureTask<RespData> futureTask = new FutureTask<>(task);
                    executor.submit(futureTask);
                    LOGGER.info("### logout application {} ###", application.getDomain());
                } else {
                    LOGGER.info("### logout application {} url is empty ###", application.getDomain());
                }
            }
        }
        return new RespData(RespCode.RESP_SUCCESS.getCode(), RespCode.RESP_SUCCESS.getMsg());
    }

    @Override
    public RespData delUser(List<Application> applicationList, LocalUser localUser) {
        if (null != applicationList && !applicationList.isEmpty()) {
            for (Application application : applicationList) {
                if (null != application.getDelUserApi()) {
                    SSODelUserTask task = new SSODelUserTask(application.getDelUserApi(), localUser.getUuid(), localUser.getUserName());
                    FutureTask<RespData> futureTask = new FutureTask<>(task);
                    executor.submit(futureTask);
                    LOGGER.info("### delUser application {} ###", application.getDomain());
                } else {
                    LOGGER.info("### delUser application {} url is empty ###", application.getDomain());
                }
            }
        }
        return new RespData(RespCode.RESP_SUCCESS.getCode(), RespCode.RESP_SUCCESS.getMsg());
    }

    @Override
    public RespData getLoginUserInfo(String token) {
        String realToken = secretService.decodeText(token);
        TokenEntity tokenEntity = cacheService.getTokenEntity(realToken);
        Map<String, Object> loginUserMap = new HashMap<>();
        if (null != tokenEntity && null != tokenEntity.getTokenValue()) {
            TokenValue tokenValue = tokenEntity.getTokenValue();
            loginUserMap.put("userName", tokenValue.getUserName());
            if (null == tokenValue.getUserType()) {
                SSOUser ssoUser = issoUserService.findByUserName(tokenValue.getUserName());
                loginUserMap.put("post", ssoUser.getUserTitle());
                loginUserMap.put("lastLoginDate", ssoUser.getLastLoginDate());
                /*loginUserMap.put("realName","");
                loginUserMap.put("email","");
                loginUserMap.put("phone","");*/
            } else {
                switch (tokenValue.getUserType()) {
                    case LDAP_USER:
                        SSOUser ssoUser = issoUserService.findByUserName(tokenValue.getUserName());
                        loginUserMap.put("post", ssoUser.getUserTitle());
                        loginUserMap.put("lastLoginDate", ssoUser.getLastLoginDate());
                        /*loginUserMap.put("realName","");
                        loginUserMap.put("email","");
                        loginUserMap.put("phone","");*/
                        break;
                    case LOCAL_USER:
                        LocalUser localUser = localUserService.findByUserName(tokenValue.getUserName());
                        loginUserMap.put("post", localUser.getPost());
                        loginUserMap.put("lastLoginDate", localUser.getLastLoginDate());
                        /*loginUserMap.put("realName",localUser.getRealName());
                        loginUserMap.put("email",localUser.getEmail());
                        loginUserMap.put("phone",localUser.getTel());*/
                        break;
                    case GOMEPLUS_USER:
                        ssoUser = issoUserService.findByUserName(tokenValue.getUserName());
                        loginUserMap.put("post", ssoUser.getUserTitle());
                        loginUserMap.put("lastLoginDate", ssoUser.getLastLoginDate());
                        /*loginUserMap.put("realName","");
                        loginUserMap.put("email","");
                        loginUserMap.put("phone","");*/
                }
            }
            return new RespData(RespCode.RESP_SUCCESS.getCode(), RespCode.RESP_SUCCESS.getMsg(), loginUserMap);
        } else {
            return new RespData(RespCode.PLEASE_LOGIN.getCode(), RespCode.PLEASE_LOGIN.getMsg());
        }
    }

    @Override
    public Object verifyUser(String userName) {
        Map<String, Object> userMap = new HashMap<>();
        LocalUser localUser = localUserService.findByUserName(userName);
        if (null == localUser) {
            LOGGER.debug("Current ldap.effect is " + ldapService.isEffect() );
            ldap_verify:if (ldapService.isEffect()) {
                SSOUser ssoUser = ldapService.findLdapUserInfo(userName);
                if (null != ssoUser) {
                    userMap.put("post", ssoUser.getUserTitle());
                    userMap.put("userName", ssoUser.getUserName());
                    userMap.put("organization", ssoUser.getOrganization());
                    userMap.put("userId", ssoUser.getLdapUserId());
                    return userMap;
                }
            }
            return RespCode.USER_NOT_EXIST.getCode();
        } else {
            userMap.put("post", localUser.getPost());
            userMap.put("userName", localUser.getUserName());
            userMap.put("organization", localUser.getCompany());
            userMap.put("userId", localUser.getUuid());
            userMap.put("password", localUser.getPassword());
            return userMap;
        }
    }

}
