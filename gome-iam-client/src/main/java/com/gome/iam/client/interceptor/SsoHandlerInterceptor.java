package com.gome.iam.client.interceptor;

import com.alibaba.fastjson.JSON;
import com.gome.iam.client.cache.TokenCache;
import com.gome.iam.client.config.GomeSSOServerConfig;
import com.gome.iam.common.util.CookieUtil;
import com.gome.iam.common.util.HttpRequestUtil;
import com.gome.iam.domain.constant.RespCode;
import com.gome.iam.domain.request.RequestData;
import com.gome.iam.domain.response.RespData;
import com.gome.iam.domain.response.VerifyRespData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.HashMap;


/**
 * @author yintongjiang
 * @params
 * @since 2016/10/18
 */
@Component("ssoHandlerInterceptor")
public class SsoHandlerInterceptor implements HandlerInterceptor {
    private final static Logger LOGGER = LoggerFactory.getLogger(TokenCache.class);
    @Autowired
    private GomeSSOServerConfig gomeSsoServerConfig;


    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                             Object o) throws Exception {
        if (gomeSsoServerConfig.hasExcludeUrl(httpServletRequest.getRequestURI())) {
            return true;
        }
        String sign = httpServletRequest.getParameter("sign");
        // 如果sign不为空说明是登录后跳转回来的
        if (null != sign && !"".equals(sign)) {
            LOGGER.info("### sign 进行反向校验 ###");
            String resp = HttpRequestUtil.doPost(gomeSsoServerConfig.getTokenUrl(),
                    RequestData.toJSONString(sign), new HashMap<String, String>() {
                        {
                            put("appKey", gomeSsoServerConfig.getAppKey());
                        }
                    });
            RespData respData = JSON.parseObject(resp, RespData.class);
            if (RespCode.RESP_SUCCESS.getCode().equals(respData.getCode())) {
                LOGGER.info("### sign 校验成功 ###");
                VerifyRespData verifyRespData = JSON.parseObject(respData.getData() + "", VerifyRespData.class);
                Integer seconds = (int) (verifyRespData.getExpireTime() - verifyRespData.getCreateTime());
                CookieUtil.setCookie(httpServletResponse, "token", sign, seconds);
                TokenCache.setTokenData(verifyRespData.getUserName(), sign, verifyRespData.getCreateTime(), verifyRespData.getExpireTime());
                httpServletRequest.getSession().setAttribute("ssoUserName", verifyRespData.getUserName());
                httpServletRequest.getSession().setAttribute("ssoUserId", verifyRespData.getUserId());
                httpServletRequest.getSession().setMaxInactiveInterval(seconds);
                return true;
            } else {
                LOGGER.info("### sign 校验失败 resp={} ###", respData.toString());
                redirect(httpServletRequest, httpServletResponse);
                return false;
            }
        }
        // 从cookie获取token并在tokencache中判断是否存在不存在就进行跳转到登录页
        String token = CookieUtil.getCookieValue(httpServletRequest, "token");
        if (null != token && TokenCache.contains(token)) {
            LOGGER.info("### token 验证成功 ###");
            return true;
        } else {
            LOGGER.info("### token 验证失败 ###");
            redirect(httpServletRequest, httpServletResponse);
            return false;
        }
    }

    private void redirect(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        StringBuffer localUrl = httpServletRequest.getRequestURL();
        String queryString = httpServletRequest.getQueryString();
        if (null != queryString && !"".equals(queryString)) {
            localUrl.append("?").append(queryString);
        }
        httpServletResponse.addHeader("location",
                gomeSsoServerConfig.getLoginUrl() + "?redirectUrl="
                        + URLEncoder.encode(localUrl.toString(), "utf-8") +
                        "&_t=" + System.currentTimeMillis());
        httpServletResponse.setStatus(302);
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }


    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
