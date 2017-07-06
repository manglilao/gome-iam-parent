package com.gome.iam.common.util;

import java.util.HashMap;
import java.util.Map;


@SuppressWarnings({ "rawtypes", "unchecked" })
public class MapUtil {
    public static Map<String, Object> stringObjectMap(Object... keyAndValues) {
        return newMap(keyAndValues);
    }


    public static Map newMap(Object... keyAndValues) {
        if (null == keyAndValues) {
            return null;
        }
        int len = keyAndValues.length;
        if (len % 2 != 0) {
            throw new RuntimeException("参数个数应该为偶数 , len=" + len);
        }
        Map map = new HashMap();
        for (int i = 0; i < len; i = i + 2) {
            map.put(keyAndValues[i], keyAndValues[i + 1]);
        }
        return map;
    }


    public static Boolean isNullOrEmpty(Map map) {
        return null == map || map.isEmpty();
    }


    public static <K, V> V getValue(Map<K, V> map, K key) {
        return null == map ? null : map.get(key);
    }


    public static <K, V> Map<K, V> remove(Map<K, V> map, String... keys) {
        if (null == map || null == keys) {
            return map;
        }

        Map result = new HashMap();
        result.putAll(map);

        for (String key : keys) {
            result.remove(key);
        }
        return result;
    }
}