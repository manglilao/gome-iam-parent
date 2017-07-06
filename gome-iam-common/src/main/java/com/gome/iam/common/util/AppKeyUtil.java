package com.gome.iam.common.util;

import java.util.UUID;

/**
 * @author luoji
 * @params
 * @since 2016/10/27 0027
 */
public class AppKeyUtil {

    public static String New() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "");
    }
}
