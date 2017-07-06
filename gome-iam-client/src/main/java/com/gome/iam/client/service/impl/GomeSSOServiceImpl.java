package com.gome.iam.client.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import com.gome.iam.domain.token.TokenData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gome.iam.client.cache.TokenCache;
import com.gome.iam.client.config.GomeSSOServerConfig;
import com.gome.iam.client.service.GomeSSOService;
import com.gome.iam.common.util.CookieUtil;


/**
 * @author yintongjiang
 * @params
 * @since 2016/10/24
 */
public class GomeSSOServiceImpl implements GomeSSOService {
    private final static Logger LOGGER = LoggerFactory.getLogger(TokenCache.class);
    @Autowired
    private GomeSSOServerConfig gomeSSOServerConfig;


    public void init() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                runTask();
            }
        }, 0, 5, TimeUnit.SECONDS);

    }


    public void removeToken(HttpServletResponse httpServletResponse, String token) {
        CookieUtil.removeCookie(httpServletResponse, "token");
        TokenCache.removeTokenData(token);
    }


    public void removeToken(String token) {
        TokenCache.removeTokenData(token);
    }


    private void runTask() {
        ConcurrentHashMap<String, TokenData> concurrentHashMap = TokenCache.getTokenDataMap();
        if (!concurrentHashMap.isEmpty()) {
            for (Map.Entry<String, TokenData> entry : concurrentHashMap.entrySet()) {
                String key = entry.getKey();
                TokenData tokenData = entry.getValue();
                if (tokenData.getExpireTime() < System.currentTimeMillis()) {
                    LOGGER.info("### token={} 已到过期时间移除 ###",tokenData.getValue());
                    concurrentHashMap.remove(key);
                }
            }
        }
    }


    public String getAppHomeUrl() {
        return gomeSSOServerConfig.getAppHomeUrl();
    }


    public String getAppKey() {
        return gomeSSOServerConfig.getAppKey();
    }


    public String getSsoLogOutUrl() {
        return gomeSSOServerConfig.getLogoutUrl();
    }
}
