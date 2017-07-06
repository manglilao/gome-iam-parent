package com.gome.iam.client.cache;


import com.gome.iam.domain.token.TokenData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yintongjiang
 * @params
 * @since 2016/10/21
 */
public class TokenCache {
    private final static Logger LOGGER = LoggerFactory.getLogger(TokenCache.class);
    private static ConcurrentHashMap<String, TokenData> tokenDataMap = new ConcurrentHashMap<String, TokenData>();

    public static void setTokenData(TokenData tokenData) {
        tokenDataMap.put(tokenData.getValue(), tokenData);
    }

    public static void setTokenData(String userName, String value, Long createTime, Long expireTime) {
        LOGGER.info("###setTokenData  value={} ###", value);
        tokenDataMap.put(value, new TokenData(userName, value, createTime, expireTime));
    }

    public static void removeTokenData(TokenData tokenData) {
        tokenDataMap.remove(tokenData.getValue());
    }

    public static void removeTokenData(String token) {
        LOGGER.info("###removeTokenData  value={} ###", token);
        tokenDataMap.remove(token);
    }

    public static TokenData getTokenData(TokenData tokenData) {
        return tokenDataMap.get(tokenData.getValue());
    }

    public static TokenData getTokenData(String token) {
        return tokenDataMap.get(token);
    }

    public static boolean contains(String token) {
        return tokenDataMap.containsKey(token);
    }

    public static ConcurrentHashMap<String, TokenData> getTokenDataMap() {
        return tokenDataMap;
    }
}
