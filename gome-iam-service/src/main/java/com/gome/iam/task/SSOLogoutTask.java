package com.gome.iam.task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import com.alibaba.fastjson.JSON;
import com.gome.iam.common.util.HttpRequestUtil;
import com.gome.iam.domain.response.RespData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author yintongjiang
 * @params
 * @since 2016/10/25
 */
public class SSOLogoutTask implements Callable<RespData> {
    private final static Logger LOGGER = LoggerFactory.getLogger(SSOLogoutTask.class);
    private String logoutUrl;
    private String token;


    public SSOLogoutTask(String logoutUrl, String token) {
        this.logoutUrl = logoutUrl;
        this.token = token;
    }


    public RespData call() throws Exception {
        Map<String, String> body = new HashMap<String, String>();
        body.put("token", token);
        Map<String, String> header = new HashMap<String, String>();
        header.put("from", "sso");
        LOGGER.info("### logoutUrl={} ###", this.logoutUrl);
        String resp = HttpRequestUtil.doPost(this.logoutUrl, JSON.toJSONString(body), header);
        LOGGER.info("### logoutUrl={} resp={} ###", this.logoutUrl,resp);
        return null;
    }
}
