package com.gome.iam.common.util;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;


public class StringUtil {
    public static String join(String linker, String prefix, String suffix, Object[] values) {
        assert null != values;
        if (values.length == 0) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < values.length - 1; i = i + 1) {
            stringBuffer.append(prefix).append(values[i]).append(suffix).append(linker);
        }
        stringBuffer.append(prefix).append(values[values.length - 1]).append(suffix);
        return stringBuffer.toString();
    }


    /**
     * @see #join(String, String, String, Object[])
     */
    public static String join(String linker, String prefix, String suffix, List<String> values) {
        return join(linker, prefix, suffix, values.toArray());
    }


    /**
     * @see #join(String, String, String, Object[])
     */
    public static String join(String linker, String prefix, String suffix, Set<String> values) {
        return join(linker, prefix, suffix, values.toArray());
    }


    /**
     * @see #join(String, String, String, Object[])
     */
    public static String join(String linker, Object[] values) {
        if (null != values && values.length > 0 && values[0].getClass().isArray()) {
            values = (Object[]) values[0]; // 传入参数是 Serializable[] 数组时,会被判断为非数组
        }
        return join(linker, "", "", values);
    }


    /**
     * @see #join(String, String, String, Object[])
     */
    @SuppressWarnings("rawtypes")
    public static String join(String linker, List values) {
        return null == values ? null : join(linker, values.toArray());
    }


    public static Boolean isNullOrEmpty(String value) {
        return null == value || value.trim().isEmpty();
    }


    public static Boolean equals(String str1, String str2) {
        return null != str1 && null != str2 && str1.equals(str2);
    }


    public static boolean equalsIgnoreCase(String str1, String str2) {
        return null != str1 && null != str2 && str1.equalsIgnoreCase(str2);
    }


    public static Boolean endsWith(String str1, String str2) {
        return null != str1 && null != str2 && str1.endsWith(str2);
    }


    public static Boolean startsWith(String string, String prefix) {
        return null != string && null != prefix && string.startsWith(prefix);
    }


    public static Boolean contains(String str1, String str2) {
        return null != str1 && null != str2 && str1.contains(str2);
    }


    public static Boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }


    public static int compareTo(String value1, String value2) {
        return (null == value1 || null == value2) ? 0 : value1.compareTo(value2);
    }


    public static String dup(String cs, int num) {
        if (null == cs || num <= 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(cs.length() * num);
        for (int i = 0; i < num; i++) {
            stringBuilder.append(cs);
        }
        return stringBuilder.toString();
    }


    public static String toString(Object object) {
        return null == object ? null : object.toString();
    }


    public static String trim(String str) {
        return null == str ? null : str.trim();
    }


    public static String changeCharset(String line, String charsetFrom, String charsetTo) {
        try {
            if (null == line || null == charsetTo) {
                return line;
            }
            else if (null == charsetFrom) {
                return new String(line.getBytes(), charsetTo);
            }
            else {
                return new String(line.getBytes(charsetFrom), charsetTo);
            }
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    public static String changeCharset(String line, String charsetTo) {
        return changeCharset(line, null, charsetTo);
    }


    public static boolean containsIgnoreCase(String str1, String str2) {
        return null != str1 && null != str2 && str1.toLowerCase().contains(str2.toLowerCase());
    }


    public static String[] split(String value, String split) {
        if (null == value || null == split) {
            return new String[] { value };
        }
        else {
            return value.split(split);
        }
    }


    public static String emptyWhenNull(String value) {
        return null == value ? "" : value;
    }
}