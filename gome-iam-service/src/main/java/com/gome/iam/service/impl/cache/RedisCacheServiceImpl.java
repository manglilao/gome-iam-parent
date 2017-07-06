package com.gome.iam.service.impl.cache;

import com.alibaba.fastjson.JSON;
import com.gome.iam.common.cache.Cache;
import com.gome.iam.common.cache.RedisCache;
import com.gome.iam.domain.token.TokenEntity;
import com.gome.iam.domain.token.TokenValue;
import com.gome.iam.domain.user.UserCode;
import com.gome.iam.service.cache.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @author luoji
 * @params
 * @since 2016/10/24 0024
 */
@Service("redisCacheServiceImpl")
public class RedisCacheServiceImpl implements CacheService {

    @Autowired
    @Qualifier("redisCache")
    private Cache<String, String> cache;
    /*@Resource
    private RedisCache<String,String> cache;*/

    /**
     * @return
     * @author jerrylou
     * @params
     * @since 2016/10/25 0025
     */
    @Override
    public boolean add2UpTokenEntity(TokenEntity tokenEntity) {
        if (tokenEntity == null) {
            return false;
        }

        String token = tokenEntity.getToken();
        if ("".equals(token)) {
            return false;
        }

        this.cache.put(token, JSON.toJSONString(tokenEntity.getTokenValue()));
        return true;
    }


    @Override
    public boolean add2UpTokenEntity(TokenEntity tokenEntity, long ttl) {
        if (tokenEntity == null) {
            return false;
        }

        String token = tokenEntity.getToken();
        if ("".equals(token)) {
            return false;
        }

        this.cache.put(token, JSON.toJSONString(tokenEntity.getTokenValue()), ttl);
        return false;
    }


    @Override
    public TokenEntity getTokenEntity(String token) {
        if (token == null) {
            return null;
        }

        if ("".equals(token)) {
            return null;
        }

        String json = this.cache.get(token);
        TokenValue tokenValue = JSON.parseObject(json, TokenValue.class);
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setToken(token);
        tokenEntity.setTokenValue(tokenValue);
        return tokenEntity;
    }


    @Override
    public void delTokenEntity(String token) {
        if (token == null) {
            return;
        }

        if ("".equals(token)) {
            return;
        }

        this.cache.remove(token);
    }


    @Override
    public boolean exist(String token) {
        if (token == null) {
            return false;
        }

        if ("".equals(token)) {
            return false;
        }

        return this.cache.exists(token);
    }

    @Override
    public void addUserCode(UserCode userCode, long time) {
        this.cache.put(userCode.getCode(), JSON.toJSONString(userCode), time);
    }
}
