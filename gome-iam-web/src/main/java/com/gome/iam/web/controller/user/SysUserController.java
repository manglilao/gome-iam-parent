package com.gome.iam.web.controller.user;


import com.gome.iam.common.constant.AesEncodeRule;
import com.gome.iam.common.controller.BaseController;
import com.gome.iam.domain.base.SessionUser;
import com.gome.iam.domain.constant.RespCode;
import com.gome.iam.domain.user.SysUser;
import com.gome.iam.service.user.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * sys用户 对外接口s
 *
 * @author gaoyanlei
 * @since 2016/10/25
 */

@RequestMapping("/sysUser")
@RestController
public class SysUserController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(SysUserController.class);
    @Autowired
    private ISysUserService sysUserService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object longin(@RequestBody SysUser sysUser, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        logger.info("### /login user={} ###",sysUser.getUserName());
        try {
            SysUser user = sysUserService.findByUserNameAndPassword(sysUser);
            if (user != null) {
                SessionUser sessionUser = new SessionUser();
                sessionUser.setUserName(user.getUserName());
                sessionUser.setSsoUserId(user.getSysUserId());
                httpServletRequest.getSession().setAttribute(AesEncodeRule.SESSION_USER, sessionUser);
                return this.ok(user);
            } else {
                return this.error(RespCode.USER_OR_PASSWORD_ERROR.getCode());
            }
        } catch (Exception e) {
            logger.error("/login error {}", e);
            return this.error(RespCode.USER_OR_PASSWORD_ERROR.getCode());
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public Object logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            logger.info("### /logout  ###");
            httpServletRequest.getSession().removeAttribute(AesEncodeRule.SESSION_USER);
            return this.ok();
        } catch (Exception e) {
        }
        return this.error(RespCode.SYSERROR.getCode());
    }

    @RequestMapping(value = "/loginUser", method = RequestMethod.GET)
    public Object test(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            return this.ok(this.getSessionUser(httpServletRequest));
        } catch (Exception e) {
            return this.error(RespCode.SYSERROR.getCode());
        }
    }
}
