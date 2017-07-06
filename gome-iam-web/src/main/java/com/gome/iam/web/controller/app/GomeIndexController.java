package com.gome.iam.web.controller.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gome.iam.common.controller.BaseController;


/**
 * @author: tianyuliang
 * @since: 2016/11/1
 */
@RestController
@RequestMapping("/")
public class GomeIndexController extends BaseController {

    /**
     * @author tianyuliang
     * @params
     * @since 2016/11/1
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void redirectIndex(HttpServletRequest request, HttpServletResponse response) {
        response.addHeader("location", "/index.html"); // mvc:resource 匹配之后，实际上是跳转到/static/index.html
        response.setStatus(302);
    }

}
