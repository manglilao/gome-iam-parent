package com.gome.iam.web.controller.app;


import com.gome.iam.common.controller.BaseController;
import com.gome.iam.domain.app.AppInfoQryParam;
import com.gome.iam.domain.app.Application;
import com.gome.iam.domain.base.SessionUser;
import com.gome.iam.domain.constant.RespCode;
import com.gome.iam.domain.response.PageData;
import com.gome.iam.domain.response.RespData;
import com.gome.iam.service.app.ApplicationInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author luoji
 * @params
 * @since 2016/10/25 0025
 */
@RestController
@RequestMapping("/appinfo")
public class GomeApplicationInfoController extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(GomeApplicationInfoController.class);
    @Autowired
    private ApplicationInfoService applicationInfoService;

    /**
     * @return 返回指定应用信息
     * @author jerrylou
     * @params
     * @since 2016/10/28 0028
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Object showApplication(HttpServletRequest httpServletRequest,
                                  @RequestParam Integer appId) {

        SessionUser sessionUser = this.getSessionUser(httpServletRequest);
        String userName = sessionUser == null ? "nologin" : sessionUser.getUserName();
        logger.info("### /appinfo/get user {} ###", userName);
        try {
            Application application = this.applicationInfoService.queryApplication(appId);
            return this.ok(application);
        } catch (Exception e) {
            logger.error("/appinfo/get error {}", e);
        }

        return this.error(RespCode.SYSERROR.getCode());
    }

    /**
     * @return 返回应用列表及总数
     * @author jerrylou
     * @params
     * @since 2016/10/27 0027
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Object showApplicationList(HttpServletRequest httpServletRequest,
                                      @RequestParam(required = false) String appName,
                                      @RequestParam(required = false, defaultValue = "0") Integer offset,
                                      @RequestParam(required = false, defaultValue = "10") Integer limit) {

        SessionUser sessionUser = this.getSessionUser(httpServletRequest);
        String userName = sessionUser == null ? "nologin" : sessionUser.getUserName();
        logger.info("### /appinfo/list user {} ###", userName);
        AppInfoQryParam appInfoQryParam = new AppInfoQryParam();
        appInfoQryParam.setAppName(appName);
        appInfoQryParam.setOffset(offset);
        appInfoQryParam.setLimit(limit);

        try {
            PageData<Application> pageData = this.applicationInfoService.queryApplications(appInfoQryParam);
            return this.ok(pageData);
        } catch (Exception e) {
            logger.error("/appinfo/list error {}", e);
        }

        return this.error(RespCode.SYSERROR.getCode());
    }

    /**
     * @return 返回添加成功的应用ID
     * @author jerrylou
     * @params
     * @since 2016/10/27 0027
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object addApplication(HttpServletRequest httpServletRequest,
                                 @RequestBody Application application) {

        SessionUser sessionUser = this.getSessionUser(httpServletRequest);
        String userName = sessionUser == null ? "nologin" : sessionUser.getUserName();
        logger.info("### /appinfo/add user {} ###", userName);
        try {
            if (application != null) {
                application.setCreator(userName);
                Integer appId = this.applicationInfoService.newApplication(application);
                return this.ok(appId);
            }
        } catch (Exception e) {
            logger.error("/appinfo/add error {}", e);
        }

        return this.error(RespCode.SYSERROR.getCode());
    }

    /**
     * @return 返回修改成功的应用ID，修改失败返回0
     * @author jerrylou
     * @params
     * @since 2016/10/27 0027
     */
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public Object modifyApplication(HttpServletRequest httpServletRequest,
                                    @RequestBody Application application) {

        SessionUser sessionUser = this.getSessionUser(httpServletRequest);
        String userName = sessionUser == null ? "nologin" : sessionUser.getUserName();
        logger.info("### /appinfo/modify user {} ###", userName);
        try {
            Integer appId = this.applicationInfoService.modifyApplication(application);
            return this.ok(appId);
        } catch (Exception e) {
            logger.error("/appinfo/modify error {}", e);
        }

        return this.error(RespCode.SYSERROR.getCode());
    }

    /**
     * @return 返回更新成功的应用Appkey，修改失败返回空字符串
     * @author jerrylou
     * @params
     * @since 2016/10/27 0027
     */
    @RequestMapping(value = "/flushkey", method = RequestMethod.POST)
    public Object flushAppKey(HttpServletRequest httpServletRequest,
                              @RequestBody Application application) {

        SessionUser sessionUser = this.getSessionUser(httpServletRequest);
        String userName = sessionUser == null ? "nologin" : sessionUser.getUserName();
        logger.info("### /appinfo/flushkey user {} ###", userName);
        try {
            String appKey = this.applicationInfoService.flushAppKey(application);
            return this.ok(appKey);
        } catch (Exception e) {
            logger.error("/appinfo/flushkey error {}", e);
        }

        return this.error(RespCode.SYSERROR.getCode());
    }
}
