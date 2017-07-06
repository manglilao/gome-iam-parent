package com.gome.iam.service.impl.api;

import com.gome.iam.dao.app.ApplicationMapper;
import com.gome.iam.domain.app.Application;
import com.gome.iam.domain.constant.AppStatus;
import com.gome.iam.domain.constant.RespCode;
import com.gome.iam.domain.response.RespData;
import com.gome.iam.domain.response.VerifyRespData;
import com.gome.iam.domain.token.TokenEntity;
import com.gome.iam.service.api.AppService;
import com.gome.iam.service.cache.CacheService;
import com.gome.iam.service.secret.SecretService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;


/**
 * @author yintongjiang
 * @params
 * @since 2016/10/24
 */
@Service
public class AppServiceImpl implements AppService {
    private final static Logger LOGGER = LoggerFactory.getLogger(AppServiceImpl.class);
    @Autowired
    private ApplicationMapper applicationMapper;
    @Autowired
    @Qualifier("redisCacheServiceImpl")
    private CacheService cacheService;
    @Autowired
    @Qualifier("blowfishSecretServiceImpl")
    private SecretService secretService;

    public List<Application> queryByStatus(Integer status) {
        return applicationMapper.findByStatus(status);
    }


    public RespData verifyAppKeyAndToken(String appKey, String token) {
        Application application = applicationMapper.findByAppkey(appKey);
        if (null == application) {
            LOGGER.error("### verifyAppKeyAndToken application is empty ###");
            return new RespData(RespCode.APP_KEY_ERROR.getCode(), RespCode.APP_KEY_ERROR.getMsg());
        }
        if (AppStatus.STOPPING.getStatus().equals(application.getStatus())) {
            LOGGER.error("### verifyAppKeyAndToken application is stopping ###");
            return new RespData(RespCode.APP_STOPPING.getCode(), RespCode.APP_STOPPING.getMsg());
        }
        String realToken = secretService.decodeText(token);
        if (null == realToken) {
            LOGGER.error("### verifyAppKeyAndToken realToken is empty ###");
            return new RespData(RespCode.TOKEN_DECODE_ERROR.getCode(), RespCode.TOKEN_DECODE_ERROR.getMsg());
        }
        LOGGER.info("### verifyAppKeyAndToken token={} realToken={} ###", token, realToken);
        TokenEntity tokenEntity = cacheService.getTokenEntity(realToken);
        if (null != tokenEntity && null != tokenEntity.getTokenValue()) {

            return new RespData(RespCode.RESP_SUCCESS.getCode(), RespCode.RESP_SUCCESS.getMsg(),
                    new VerifyRespData(tokenEntity.getTokenValue().getUserId(), tokenEntity.getTokenValue().getUserName(),
                            tokenEntity.getTokenValue().getExpireTime(), tokenEntity.getTokenValue().getCreateTime(),tokenEntity.getTokenValue().getDomain()));
        } else {
            LOGGER.error("### verifyAppKeyAndToken tokenEntity is empty ###");
            return new RespData(RespCode.TOKEN_INVALID.getCode(), RespCode.TOKEN_INVALID.getMsg());
        }
    }
    public RespData verifyUrlAndToken(String redirectUrl, String token) {
        LOGGER.info("### verifyUrl redirectUrl={} ###",redirectUrl);
        URL url = null;
        try {
            url = new URL(redirectUrl);
        } catch (Exception e) {
            LOGGER.error("### verifyUrl url is error={} ###",e);
        }
        if (null == url|| applicationMapper.countByDomain(url.getHost(),AppStatus.RUNNING.getStatus())==0) {
            return new RespData(RespCode.REDIRECT_URL_INVALID.getCode(), RespCode.REDIRECT_URL_INVALID.getMsg());
        }
        String realToken = secretService.decodeText(token);
        if (null == realToken) {
            LOGGER.error("### verifyAppKeyAndToken realToken is empty ###");
            return new RespData(RespCode.TOKEN_DECODE_ERROR.getCode(), RespCode.TOKEN_DECODE_ERROR.getMsg());
        }
        LOGGER.info("### verifyAppKeyAndToken token={} realToken={} ###", token, realToken);
        TokenEntity tokenEntity = cacheService.getTokenEntity(realToken);
        if (null != tokenEntity && null != tokenEntity.getTokenValue()) {

            return new RespData(RespCode.RESP_SUCCESS.getCode(), RespCode.RESP_SUCCESS.getMsg(),
                    new VerifyRespData(tokenEntity.getTokenValue().getUserId(), tokenEntity.getTokenValue().getUserName(),
                            tokenEntity.getTokenValue().getExpireTime(), tokenEntity.getTokenValue().getCreateTime(),tokenEntity.getTokenValue().getDomain()));
        } else {
            LOGGER.error("### verifyAppKeyAndToken tokenEntity is empty ###");
            return new RespData(RespCode.TOKEN_INVALID.getCode(), RespCode.TOKEN_INVALID.getMsg());
        }
    }

    public RespData verifyAppKeyAndUrl(String appKey, String redirectUrl) {
        LOGGER.info("### verifyAppKeyAndUrl appKey={}   redirectUrl={} ###",appKey,redirectUrl);
        if (null == appKey) {
            return new RespData(RespCode.APP_KEY_BLANK.getCode(), RespCode.APP_KEY_BLANK.getMsg());
        }
        Application application = applicationMapper.findByAppkey(appKey);
        URL url = null;
        try {
            url = new URL(redirectUrl);
        } catch (Exception e) {
            LOGGER.error("### verifyAppKeyAndUrl url is error={} ###",e);
        }
        if (null == application) {
            return new RespData(RespCode.APP_KEY_ERROR.getCode(), RespCode.APP_KEY_ERROR.getMsg());
        }
        if (AppStatus.STOPPING.getStatus().equals(application.getStatus())) {
            return new RespData(RespCode.APP_STOPPING.getCode(), RespCode.APP_STOPPING.getMsg());
        }
        if (null != url && url.getHost().equals(application.getDomain())) {
            return new RespData(RespCode.RESP_SUCCESS.getCode(), RespCode.RESP_SUCCESS.getMsg());
        } else {
            return new RespData(RespCode.APP_KEY_NOT_MATCH.getCode(), RespCode.APP_KEY_NOT_MATCH.getMsg());
        }
    }

    @Override
    public RespData verifyUrl( String redirectUrl) {
        LOGGER.info("### verifyUrl redirectUrl={} ###",redirectUrl);
        URL url = null;
        try {
            url = new URL(redirectUrl);
        } catch (Exception e) {
            LOGGER.error("### verifyUrl url is error={} ###",e);
        }
        if (null != url&& applicationMapper.countByDomain(url.getHost(),AppStatus.RUNNING.getStatus())>0) {
            return new RespData(RespCode.RESP_SUCCESS.getCode(), RespCode.RESP_SUCCESS.getMsg());
        }else{
            return new RespData(RespCode.REDIRECT_URL_INVALID.getCode(), RespCode.REDIRECT_URL_INVALID.getMsg());
        }
    }
}
