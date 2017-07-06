package com.gome.iam.service.app;

import com.gome.iam.domain.app.AppInfoQryParam;
import com.gome.iam.domain.app.Application;
import com.gome.iam.domain.response.PageData;

import java.util.List;


/**
 * @author luoji
 * @params
 * @since 2016/10/25 0025
 */
public interface ApplicationInfoService {

    Application queryApplication(Integer appId) throws Exception;

    PageData<Application> queryApplications(AppInfoQryParam appInfoQryParam) throws Exception;

    Integer newApplication(Application application) throws Exception;

    Integer modifyApplication(Application application) throws Exception;

    String flushAppKey(Application application) throws Exception;

    /**
     * 查询所有启用的应用列表
     *
     * @return
     */
    List<Application> queryApplications();
}
