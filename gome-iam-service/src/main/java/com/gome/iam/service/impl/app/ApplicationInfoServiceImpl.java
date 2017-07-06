package com.gome.iam.service.impl.app;

import com.gome.iam.common.util.AppKeyUtil;
import com.gome.iam.dao.app.ApplicationMapper;
import com.gome.iam.domain.app.AppInfoQryParam;
import com.gome.iam.domain.app.Application;
import com.gome.iam.domain.constant.AppStatus;
import com.gome.iam.domain.response.PageData;
import com.gome.iam.service.app.ApplicationInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author luoji
 * @params
 * @since 2016/10/26 0026
 */
@Service
public class ApplicationInfoServiceImpl implements ApplicationInfoService {
    private final static Logger logger = LoggerFactory.getLogger(ApplicationInfoServiceImpl.class);

    @Autowired
    private ApplicationMapper applicationMapper;


    /**
     * @return 返回应用信息
     * @author jerrylou
     * @params
     * @since 2016/10/28 0028
     */
    @Override
    public Application queryApplication(Integer appId) throws Exception {
        if (appId == null) {
            throw new Exception("get application param error.");
        }

        logger.info("queryApplication param {}", appId);
        Application application = this.applicationMapper.find(appId);
        logger.info("queryApplication result {}", application);

        return application;
    }

    /**
     * @return 返回应用列表及总数
     * @author jerrylou
     * @params
     * @since 2016/10/27 0027
     */
    @Override
    public PageData<Application> queryApplications(AppInfoQryParam appInfoQryParam) throws Exception {

        if (appInfoQryParam == null || appInfoQryParam.getOffset() == null
                || appInfoQryParam.getLimit() == null) {
            throw new Exception("get application list param error.");
        }

        logger.info("queryApplications param {}", appInfoQryParam);
        PageData<Application> pageData = null;
        // 查询应用
        String appName = appInfoQryParam.getAppName();
        if (appName == null || "".equals(appName)) {
            List<Application> list = this.applicationMapper.findByPage(appInfoQryParam.getOffset(),
                    appInfoQryParam.getLimit());
            int total = this.applicationMapper.count();

            pageData = new PageData<Application>(list, total);
        } else {
            List<Application> list = this.applicationMapper.findByAppName(appInfoQryParam.getAppName(),
                    appInfoQryParam.getOffset(), appInfoQryParam.getLimit());
            int total = this.applicationMapper.countByAppName(appInfoQryParam.getAppName());
            pageData = new PageData<Application>(list, total);
        }
        logger.info("queryApplications result {}", pageData.getTotal());

        return pageData;
    }


    /**
     * @return 返回添加成功的应用ID
     * @author jerrylou
     * @params
     * @since 2016/10/27 0027
     */
    @Override
    public Integer newApplication(Application application) throws Exception {
        if (application == null || application.getAppName() == null
                || application.getDomain() == null) {
            throw new Exception("add application param error.");
        }

        logger.info("newApplication param {}", application);
        application.setStatus(AppStatus.RUNNING.getStatus());
        application.setAppKey(AppKeyUtil.New());
        this.applicationMapper.save(application);
        logger.info("newApplication result {}", application.getAppId());

        return application.getAppId();
    }


    /**
     * @return 返回修改成功的应用ID，修改失败返回0
     * @author jerrylou
     * @params
     * @since 2016/10/27 0027
     */
    @Override
    public Integer modifyApplication(Application application) throws Exception {
        if (application == null || application.getAppName() == null
                || application.getDomain() == null || application.getAppId() == null) {
            throw new Exception("modify application param error.");
        }

        logger.info("modifyApplication param {}", application);
        Application oldApplication = this.applicationMapper.find(application.getAppId());
        if (oldApplication == null) {
            logger.warn("modifyApplication result data not found");
            return 0;
        }

        this.applicationMapper.update(application);
        logger.info("modifyApplication result {}", application.getAppId());
        return application.getAppId();
    }


    /**
     * @return 返回更新成功的应用Appkey，修改失败返回空字符串
     * @author jerrylou
     * @params
     * @since 2016/10/27 0027
     */
    @Override
    public String flushAppKey(Application application) throws Exception {
        if (application == null || application.getAppId() == null) {
            throw new Exception("flush appKey application param error.");
        }

        logger.info("flushAppKey param {}", application);
        Application oldApplication = this.applicationMapper.find(application.getAppId());
        if (oldApplication == null) {
            logger.warn("flushAppKey result data not found");
            return "";
        }

        Application newApplication = new Application();
        newApplication.setAppId(application.getAppId());
        newApplication.setAppKey(AppKeyUtil.New());
        this.applicationMapper.update(newApplication);
        logger.info("flushAppKey result {}", application.getAppKey());
        return newApplication.getAppKey();
    }

    @Override
    public List<Application> queryApplications() {
        return this.applicationMapper.findAll();
    }
}
