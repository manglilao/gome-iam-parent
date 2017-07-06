package com.gome.iam.common.cache;

/**
 * @author luoji
 * @params
 * @since 2016/10/29 0029
 */
public interface Cache<K, V> {

    void put(K key, V value);

    void put(K key, V value, long expire);

    Boolean expire(K key, long expire);

    V get(K key);

    Long getExpire(K key);

    void remove(K key);

    boolean exists(K key);
}
