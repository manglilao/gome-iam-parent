package com.gome.iam.web.controller.user;


import com.gome.iam.common.controller.BaseController;
import com.gome.iam.domain.constant.RespCode;
import com.gome.iam.domain.user.SSOUser;
import com.gome.iam.service.ldap.LDAPService;
import com.gome.iam.service.user.ISSOUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * sso测试接口
 *
 * @author gaoyanlei
 * @since 2016/10/25
 */

@RequestMapping("/ssoUser")
@RestController
public class SSOUserController extends BaseController {
    @Autowired
    private ISSOUserService ssoUserService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object save(@RequestBody SSOUser ssoUser) {
        try {
            Long id = ssoUserService.save(ssoUser);
            if (id instanceof Long) {
                return this.ok();
            } else {
                return this.error(RespCode.SYSERROR.getCode());
            }
        } catch (Exception e) {
            return this.error(500);
        }
    }

    @RequestMapping(value = "login", method = RequestMethod.PUT)
    public Object login(@RequestParam String userName, @RequestParam String password) {
        return null;
    }

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public Object login() {
        return ssoUserService.findByUserName("gaoyanlei");
    }

}
