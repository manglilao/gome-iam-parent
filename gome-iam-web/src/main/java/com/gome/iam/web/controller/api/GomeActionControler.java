package com.gome.iam.web.controller.api;

import com.gome.iam.common.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by qiaowentao on 2017/7/3.
 */
@Controller
@RequestMapping(value = "/index")
public class GomeActionControler extends BaseController {
    private Logger LOG = LoggerFactory.getLogger("GomeActionControler");

    /**
     * 去往登陆页面
     * @param model
     * @param redirectUrl
     * @return
     */
    @RequestMapping(value = "/toLogin",method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView toLogin(ModelAndView model, @Param("redirectUrl") String redirectUrl){
        LOG.info("## toLogin info ##redirectUrl-{}",redirectUrl);
        model.setViewName("login");
        if(redirectUrl != null && !"".equals(redirectUrl)){
            model.addObject("redirectUrl",redirectUrl);
        }
        return model;
    }

    /**
     * 去往重置密码页面
     * @param model
     * @param redirectUrl
     * @return
     */
    @RequestMapping(value = "/toResetPwd",method = RequestMethod.GET)
    public ModelAndView toResetPwd(ModelAndView model,@Param("redirectUrl") String redirectUrl){
        LOG.info("## toResetPwd info ## redirectUrl-{}",redirectUrl);
        model.setViewName("resetPwd");
        model.addObject("redirectUrl",redirectUrl);
        return model;
    }

    /**
     * 去往注册页面
     * @param model
     * @param redirectUrl
     * @return
     */
    @RequestMapping(value = "/toRegister",method = RequestMethod.GET)
    public ModelAndView toRegister(ModelAndView model,@Param("redirectUrl") String redirectUrl){
        LOG.info("## toRegister info ## redirectUrl-{}",redirectUrl);
        model.addObject("redirectUrl",redirectUrl);
        model.setViewName("userRegister");
        return model;
    }

    @RequestMapping(value = "/toServiceAgreement.html",method = RequestMethod.GET)
    public ModelAndView toServiceAgreement(ModelAndView model,@Param("redirectUrl") String redirectUrl){
        LOG.info("## toServiceAgreement info ## redirectUrl-{}",redirectUrl);
        model.addObject("redirectUrl",redirectUrl);
        model.setViewName("common/serviceAgreement");
        return model;
    }

}
