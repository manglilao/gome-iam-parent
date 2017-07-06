package com.gome.iam.common.controller;

import com.gome.iam.common.constant.AesEncodeRule;
import com.gome.iam.domain.base.SessionUser;
import com.gome.iam.domain.response.RespData;

import javax.servlet.http.HttpServletRequest;


/**
 * BaseController
 *
 * @author gaoyanlei
 * @since 2016/10/26
 */
public class BaseController {

    public Object ok(Object data) {
        return RespData.ok(data);
    }

    public Object error(int code) {
        return RespData.error(code);
    }

    public Object ok() {
        return RespData.ok(null);
    }

    public SessionUser getSessionUser(HttpServletRequest httpServletRequest) {
        return (SessionUser) httpServletRequest.getSession().getAttribute(AesEncodeRule.SESSION_USER);
    }

    public static String getRemoteIP(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(request.getRemoteAddr());
        String forwardedIp = request.getHeader("x-forwarded-for");
        if (null != forwardedIp && !"unknown".equalsIgnoreCase(forwardedIp)) {
            builder.append(",").append(forwardedIp);
        }
        builder.trimToSize();
        return builder.toString();
    }
}