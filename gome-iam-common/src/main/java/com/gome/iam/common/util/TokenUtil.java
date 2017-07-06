package com.gome.iam.common.util;

import java.util.Map;


/**
 * @author yintongjiang
 * @params
 * @since 2016/10/21
 */
public class TokenUtil {
    public static String create(String appKey, String uid) {
        Map<String, Object> signMap = MapUtil.stringObjectMap("timestamp", System.currentTimeMillis(),
            "appKey", appKey, "uid", uid);
        String signString = SignUtil.sign(signMap, appKey);
        return Md5Util.encrypt(signString);
    }
    public static String create(String uid) {
        Map<String, Object> signMap = MapUtil.stringObjectMap("timestamp", System.currentTimeMillis(),
                 "uid", uid);
        String signString = SignUtil.sign(signMap,"appKey");
        return Md5Util.encrypt(signString);
    }

    public static void main(String[] args) {
        System.out.println(TokenUtil.create("3"));
    }
}
