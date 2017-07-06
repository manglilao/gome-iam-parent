package com.gome.iam.common.util;

import java.security.MessageDigest;


public class Md5Util {
    public static String encrypt(String value) {
        try {
            MessageDigest digist = MessageDigest.getInstance("MD5");
            byte[] bytes = digist.digest(value.getBytes("UTF-8"));
            StringBuffer digestHexStr = new StringBuffer();
            for (int i = 0; i < 16; i++) {
                String byteHex = byteHex(bytes[i]);
                digestHexStr.append(byteHex);
            }
            return digestHexStr.toString();
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }


    private static String byteHex(byte ib) {
        char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] ob = new char[2];
        ob[0] = digit[(ib >>> 4) & 0X0F];
        ob[1] = digit[ib & 0X0F];
        return new String(ob);
    }
}