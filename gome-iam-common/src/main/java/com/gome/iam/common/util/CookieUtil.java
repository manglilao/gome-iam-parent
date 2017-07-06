package com.gome.iam.common.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author yintongjiang
 * @params
 * @since 2016/10/19
 */
public class CookieUtil {
    public static void removeCookies(HttpServletResponse response, String... names) {
        if (null != names) {
            for (String name : names) {
                removeCookie(response, name);
            }
        }
    }


    public static void removeCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    public static void setCookie(HttpServletResponse response, String name, String value, int expiry,
            String path, String domain) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(expiry);
        cookie.setPath(path);
        if (null != domain) {
            cookie.setDomain(domain);
        }
        response.addCookie(cookie);
    }


    public static void setCookie(HttpServletResponse response, String name, String value, int expiry) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(expiry);
        response.addCookie(cookie);
    }


    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (StringUtil.equalsIgnoreCase(cookie.getName(), name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}