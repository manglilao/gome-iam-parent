package com.gome.iam.task;

import com.gome.iam.common.util.HttpRequestUtil;
import com.gome.iam.domain.response.RespData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * @author yintongjiang
 * @params
 * @since 2016/11/21
 */
public class SSODelUserTask implements Callable<RespData> {
    private final static Logger LOGGER = LoggerFactory.getLogger(SSOLogoutTask.class);
    private String delUserApi;
    private String userId;
    private String userName;


    public SSODelUserTask(String delUserApi, String userId, String userName) {
        this.delUserApi = delUserApi;
        this.userId = userId;
        this.userName = userName;
    }


    public RespData call() throws Exception {
        LOGGER.info("### delUserApi={} ###", this.delUserApi);
        String params = "/" + this.userId + "/" + this.userName;
        if (this.delUserApi.endsWith("/")) {
            params = this.userId + "/" + this.userName;
        }
        String resp = HttpRequestUtil.doDelete(this.delUserApi + params);
        LOGGER.info("### delUserApi={} resp={} ###", this.delUserApi, resp);
        return null;
    }
}