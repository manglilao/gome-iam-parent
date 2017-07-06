package com.gome.iam.service.cache;

import com.gome.iam.domain.token.TokenEntity;
import com.gome.iam.domain.user.UserCode;


/**
 * @author luoji
 * @params
 * @since 2016/10/24 0024
 */
public interface CacheService {

    boolean add2UpTokenEntity(TokenEntity tokenEntity);


    boolean add2UpTokenEntity(TokenEntity tokenEntity, long ttl);


    TokenEntity getTokenEntity(final String token);


    void delTokenEntity(final String token);


    boolean exist(final String token);

    void addUserCode(UserCode userCode,long time);
}
