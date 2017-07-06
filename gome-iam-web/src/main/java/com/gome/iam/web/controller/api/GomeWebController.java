package com.gome.iam.web.controller.api;

import com.gome.iam.common.constant.AesEncodeRule;
import com.gome.iam.common.controller.BaseController;
import com.gome.iam.domain.base.SessionUser;
import com.gome.iam.domain.constant.RespCode;
import com.gome.iam.domain.user.SysUser;
import com.gome.iam.service.app.ApplicationInfoService;
import com.gome.iam.service.user.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by qiaowentao on 2017/7/4.
 */
@RestController
@RequestMapping(value = "/web")
public class GomeWebController extends BaseController {
    private static Logger LOG = LoggerFactory.getLogger("GomeWebController");

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ApplicationInfoService applicationInfoService;

    //去往后台管理登录页面
    @RequestMapping(value = "/toLogin",method = RequestMethod.GET)
    public ModelAndView toWebLogin(ModelAndView model){
        model.setViewName("web/login");
        return model;
    }

    //后台管理员登录
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Object loginConfirm(HttpServletRequest httpServletRequest, @RequestBody SysUser sysUser){
        LOG.info("## web/login info ## userName-{}",sysUser.getUserName());
        try {
            SysUser user = sysUserService.findByUserNameAndPassword(sysUser);
            if (user != null) {
                SessionUser sessionUser = new SessionUser();
                sessionUser.setSsoUserId(user.getSysUserId());
                sessionUser.setUserName(user.getUserName());
                httpServletRequest.getSession().setAttribute(AesEncodeRule.SESSION_USER, sessionUser);
                return this.ok(user);
            } else {
                return this.error(RespCode.USER_OR_PASSWORD_ERROR.getCode());
            }
        } catch (Exception e) {
            LOG.error("## web/login error ## {}", e);
            return this.error(RespCode.USER_OR_PASSWORD_ERROR.getCode());
        }
    }

    //去往应用管理页面
    @RequestMapping(value = "/appList",method = {RequestMethod.POST,RequestMethod.GET})
    public ModelAndView loginSuccess(ModelAndView model, @Param("userId") String userId){
        LOG.info("## loginSuccess info ## userId-{}",userId);
        model.setViewName("web/appList");
        return model;
    }

    //去往用户信息页面
    @RequestMapping(value = "/userList",method = RequestMethod.GET)
    public ModelAndView toUserList(ModelAndView model){
        LOG.info("## toUserList info ##");
        model.setViewName("web/userList");
        return model;
    }

    //去往FAQ页面
    @RequestMapping(value = "/faq",method = RequestMethod.GET)
    public ModelAndView toFaq(ModelAndView model){
        LOG.info("## toFaq ## info ");
        model.setViewName("web/faq");
        return model;
    }

    //查询用户信息
    @RequestMapping(value = "/loginUser", method = RequestMethod.GET)
    public Object test(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            return this.ok(this.getSessionUser(httpServletRequest));
        } catch (Exception e) {
            return this.error(RespCode.SYSERROR.getCode());
        }
    }

    //去往app 增加页面
    @RequestMapping(value = "/appAdd",method = RequestMethod.GET)
    public ModelAndView toAppAdd(ModelAndView model){
        LOG.info("## toAppAdd ## info");
        model.setViewName("web/appAdd");
        return model;
    }

    @RequestMapping(value = "/appUpdate",method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView toAppUpdate(ModelAndView model, @Param("appId") Integer appId){
        LOG.info("## appUpdate info ## appId-{}",appId);
        model.addObject("appId",appId);
        model.setViewName("web/appUpdate");
        return model;
    }

    //去往添加人员页面
    @RequestMapping(value = "/userAdd",method = RequestMethod.GET)
    public ModelAndView toUserAdd(ModelAndView model){
        LOG.info("## toUserAdd info ## ");
        model.setViewName("web/userAdd");
        return model;
    }

    //去往用户信息修改页面
    @RequestMapping(value = "/userUpdate",method = RequestMethod.GET)
    public ModelAndView toUserUpdate(ModelAndView model,@Param("id") Integer id){
        LOG.info("## toUserUpdate ## info id-{}",id);
        model.addObject("id",id);
        model.setViewName("web/userUpdate");
        return model;
    }

    //后台管理员退出
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public Object logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            LOG.info("### /logout info ###");
            httpServletRequest.getSession().removeAttribute(AesEncodeRule.SESSION_USER);
            return this.ok();
        } catch (Exception e) {
            LOG.error("### /logout error ### {}",e);
            return this.error(RespCode.SYSERROR.getCode());
        }
    }

}
