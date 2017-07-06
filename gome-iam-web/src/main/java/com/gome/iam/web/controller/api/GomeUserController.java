package com.gome.iam.web.controller.api;

import com.gome.iam.common.controller.BaseController;
import com.gome.iam.service.api.SSOService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * api接口--SSO用户
 *
 * @author gaoyanlei
 * @since 2016/11/18
 */
@RestController
@RequestMapping("/api/user")
public class GomeUserController extends BaseController {
    @Autowired
    private SSOService ssoService;
    private final static Logger logger = LoggerFactory.getLogger(GomeAppController.class);

    @RequestMapping(value = "/auth_sso_user", method = RequestMethod.GET)
    public Object verifyUser(HttpServletRequest httpServletRequest, @RequestParam String userName) {
        logger.info("### 验证用户在sso系统中存在 userName={} ###", userName);
        Object object = ssoService.verifyUser(userName);
        if (object instanceof Integer) {
            return this.error((Integer) object);
        } else {
            return this.ok(object);
        }
    }
}
