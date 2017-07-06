package com.gome.iam.common.util;

import java.util.*;
import java.util.Map.Entry;


public class SignUtil {
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <K, V> String sign(Map<K, V> parameters, String appSecret) {
        Map<K, V> map = new HashMap<K, V>();
        if (null != parameters) {
            map.putAll(parameters);
        }
        return sign(map);
    }


    private static <K, V> String sign(Map<K, V> map) {
        String toSignString = toSignString(map);
        return Md5Util.encrypt(toSignString);
    }


    @SuppressWarnings("rawtypes")
    private static <K, V> String toSignString(Map<K, V> parameters) {
        List<String> list = new ArrayList<String>();
        if (null != parameters) {
            for (Entry<K, V> entry : parameters.entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                if (null != key && null != value) { // 空值不要
                    if (value.getClass().isArray()) {
                        for (Object each : (Object[]) value) {
                            list.add(key + "=" + each);
                        }
                    }
                    else if (value instanceof Collection) {
                        for (Object each : (Collection) value) {
                            list.add(key + "=" + each);
                        }
                    }
                    else {
                        list.add(key + "=" + value);
                    }
                }
            }
        }

        Collections.sort(list);

        return StringUtil.join("&", "", "", list);
    }
}