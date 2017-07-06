package com.gome.iam.common.util;

/**
 * Created by qiaowentao on 2017/6/15.
 */
public class CodeUtil {

    /**
     * 随机获取六位验证码
     * @return
     */
    public static String getRandom() {
        String num = "";
        for (int i = 0 ; i < 6 ; i ++) {
            num = num + String.valueOf((int) Math.floor(Math.random() * 9 + 1));
        }
        return num;
    }

}
