package com.gome.iam.common.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author luoji
 * @params
 * @since 2016/10/29 0029
 */
public class RedisCache<K, V> implements Cache<K, V> {
    protected static int DefaultTTL = 10;

    /*@Autowired
    @Qualifier("redisTemplate")*/
    @Resource
    private RedisTemplate<K, V> redisTemplate;

    @Override
    public void put(K key, V value) {
        this.redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void put(K key, V value, long expire) {
        if (expire <= 0) {
            expire = this.DefaultTTL;
        }

        this.redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    @Override
    public V get(K key) {
        return this.redisTemplate.opsForValue().get(key);
    }

    @Override
    public Boolean expire(K key, long expire) {
        if (expire <= 0) {
            expire = DefaultTTL;
        }

        return this.redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    @Override
    public Long getExpire(K key) {
        return this.redisTemplate.getExpire(key);
    }

    @Override
    public void remove(K key) {
        this.redisTemplate.delete(key);
    }

    @Override
    public boolean exists(K key) {
        return this.redisTemplate.hasKey(key);
    }
}
