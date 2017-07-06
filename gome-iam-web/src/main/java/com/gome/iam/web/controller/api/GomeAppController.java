package com.gome.iam.web.controller.api;

import com.alibaba.fastjson.JSON;
import com.gome.iam.common.controller.BaseController;
import com.gome.iam.common.gomeplus.JsonUtils;
import com.gome.iam.common.secret.SensitivewordFilter;
import com.gome.iam.common.util.*;
import com.gome.iam.domain.base.SessionUser;
import com.gome.iam.domain.constant.RespCode;
import com.gome.iam.domain.constant.UserType;
import com.gome.iam.domain.response.RespData;
import com.gome.iam.domain.response.RespResult;
import com.gome.iam.domain.token.TokenEntity;
import com.gome.iam.domain.token.TokenValue;
import com.gome.iam.domain.user.*;
import com.gome.iam.service.api.AppService;
import com.gome.iam.service.api.SSOService;
import com.gome.iam.service.app.ApplicationInfoService;
import com.gome.iam.service.cache.CacheService;
import com.gome.iam.service.ldap.LDAPService;
import com.gome.iam.service.secret.SecretService;
import com.gome.iam.service.user.LocalUserService;
import com.gome.iam.service.user.UserCodeInfoService;
import com.gome.iam.service.user.UserErrorCountService;
import com.gome.iam.service.user.UserPwdHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author yintongjiang
 * @params
 * @since 2016/10/18
 */
@RestController
@RequestMapping("/app")
public class GomeAppController extends BaseController {
    private final static Logger LOGGER = LoggerFactory.getLogger(GomeAppController.class);
    @Autowired
    private AppService appService;
    @Autowired
    private SSOService ssoService;
    @Autowired
    private LDAPService ldapService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private LocalUserService localUserService;
    @Autowired
    @Qualifier("blowfishSecretServiceImpl")
    private SecretService secretService;
    @Autowired
    private ApplicationInfoService applicationInfoService;
    @Value("#{config['cookie.maxAge']}")
    private Integer MAX_AGE;
    @Value("#{ldapConfig['sendUserEmail.url']}")
    private String emailUrl;
    @Value("#{ldapConfig['sendUserSms.url']}")
    private String smsUrl;
    @Value("#{ldapConfig['sendUserCode.time']}")
    private Integer CODE_TIME;
    @Autowired
    private UserCodeInfoService userCodeInfoService;
    @Autowired
    private UserPwdHistoryService userPwdHistoryService;
    @Autowired
    private UserErrorCountService userErrorCountService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public void checkRedirect(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        LOGGER.info("### app/login get ###");
        String redirectUrl = httpServletRequest.getParameter("redirectUrl");
        RespData respData = appService.verifyUrl(redirectUrl);
        if (RespCode.RESP_SUCCESS.getCode().equals(respData.getCode())) {
            redirectUrl = redirectUrl.replaceAll("[?,&]sign=.*", "");
            String token = CookieUtil.getCookieValue(httpServletRequest, "token");
            if (null != token) {
                String realToken = secretService.decodeText(token);
                TokenEntity tokenEntity = cacheService.getTokenEntity(realToken);
                if (null != realToken && null != tokenEntity && null != tokenEntity.getTokenValue()) {
                    if (redirectUrl.contains("?")) {
                        redirectUrl = redirectUrl + "&sign=" + token;
                    } else {
                        redirectUrl = redirectUrl + "?sign=" + token;
                    }
                    redirectUrl = redirectUrl + "&_t=" + System.currentTimeMillis();
                    httpServletResponse.addHeader("location", redirectUrl);
                } else {
                    try {
                        httpServletResponse.addHeader("location", "/index/toLogin?redirectUrl=" + URLEncoder.encode(redirectUrl, "utf-8"));
                    } catch (Exception e) {
                        LOGGER.error("###app/login get URLEncoder.encode error={}##", e);
                    }
                }
            } else {
                try {
                    httpServletResponse.addHeader("location", "/index/toLogin?redirectUrl=" + URLEncoder.encode(redirectUrl, "utf-8"));
                } catch (Exception e) {
                    LOGGER.error("###app/login get URLEncoder.encode error={}##", e);
                }
            }
        } else {
            LOGGER.error("### app/login get error={} ###", "url校验失败");
            httpServletResponse.addHeader("location", getFailHtml(respData.getCode()));
        }
        httpServletResponse.setStatus(302);
    }

    @ResponseBody
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public RespData login(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestBody SSOUserExt user) {
        String domain ="";
        LOGGER.info("### app/login post ###");
        String userName = user.getUserName();
        String password = user.getPassword();
        Map<String, Object> loginUserMap = new HashMap<>();
        loginUserMap.put("userName", userName);
        boolean authFlag = false;
        UserType userType = null;
        String userId = null;
        LocalUser localUser = localUserService.selectUserWithUsernameOrTelOrEmail(userName);
        LocalDateTime nowTime = LocalDateTime.now();
        if(localUser != null){
            Date lockedEndTime = localUser.getLockedEndTime();
            //查看用户是否被禁用
            if(localUser.getStatus() == 1){
                //没有被禁用
                //查看账号是否在冻结期间
                if(lockedEndTime.compareTo(new Date()) >= 0){
                    //账号冻结期间
                    return new RespData(RespCode.USER_FREEZE.getCode(),RespCode.USER_FREEZE.getMsg()+",请于"+Time7Util.reverseDateToTime(lockedEndTime)+"后重试");
                }else{
                    //不在冻结期间,判断密码是否正确
                    if(password.equals(localUser.getPassword())){
                        domain = "local_user";
                        userType = UserType.LOCAL_USER;
                        loginUserMap.put("post", localUser.getPost());
                        loginUserMap.put("lastLoginDate", localUser.getLastLoginDate());
                        userId = localUser.getUuid();
                        loginUserMap.put("userId",userId);
                        authFlag = true;
                        localUserService.updateLoginTime(localUser.getId());
                    }else{
                        String uid = localUser.getUuid();
                        //查询用户在30分钟内输错密码次数
                        LocalDateTime endTime = TimeUtil.reverseDateToLocalDateTime(Time7Util.getScheduleTime(new Date(),30));
                        int count = userErrorCountService.selectErrorCount(new UserErrorCount(null,uid,endTime));
                        if(count < 4){
                            //输错密码还不够5次
                            userErrorCountService.addOneNode(new UserErrorCount(null,uid,nowTime));
                        }else{
                            //输错密码超过五次了
                            userErrorCountService.addOneNode(new UserErrorCount(null,uid,nowTime));
                            LocalUser updateUser = new LocalUser();
                            updateUser.setId(localUser.getId());
                            updateUser.setLockedEndTime(TimeUtil.reverseLocalDateTimeToDate(nowTime.plus(1, ChronoUnit.HOURS)));
                            localUserService.updateLockedEndTime(updateUser);
                            return new RespData(RespCode.USER_FREEZE.getCode(),RespCode.USER_FREEZE.getMsg()+",请于"+TimeUtil.parseTime(nowTime)+"后重试");
                        }

                    }
                }

            }else{
                // ==0 禁用
                return new RespData(RespCode.USER_FORBIDDEN.getCode(),RespCode.USER_FORBIDDEN.getMsg());
            }


        }else{
            return new RespData(RespCode.USER_NOT_EXIST.getCode(),RespCode.USER_NOT_EXIST.getMsg());
        }

        if (authFlag) {
            String token = TokenUtil.create(userName);
            String redirectUrl = user.getRedirectUrl();
            try {
                //重复登录先删除前之前用户
                String oriToken = CookieUtil.getCookieValue(httpServletRequest, "token");
                if (null != oriToken) {
                    String realOriToken = secretService.decodeText(oriToken);
                    cacheService.delTokenEntity(realOriToken);
                    ssoService.ssoLogout(oriToken);
                }
                String enCToken = secretService.encodeText(token);
                String urlEnCToken = URLEncoder.encode(enCToken, "utf-8");
                CookieUtil.setCookie(httpServletResponse, "token", enCToken, MAX_AGE);
                TokenEntity tokenEntity = new TokenEntity();
                tokenEntity.setToken(token);
                //appKey去掉
                tokenEntity.setTokenValue(new TokenValue(null, userType, userId, userName, System.currentTimeMillis() + MAX_AGE * 1000, System.currentTimeMillis(),domain));
                cacheService.add2UpTokenEntity(tokenEntity, MAX_AGE);
                LOGGER.info("### app/login post token={}  realToken ###", enCToken, token);
                if (!StringUtil.isNullOrEmpty(redirectUrl)) {
                    redirectUrl = redirectUrl.replaceAll("[?,&]sign=.*", "");
                    if (redirectUrl.contains("?")) {
                        redirectUrl = redirectUrl + "&sign=" + urlEnCToken;
                    } else {
                        redirectUrl = redirectUrl + "?sign=" + urlEnCToken;
                    }
                    redirectUrl = redirectUrl + "&_t=" + System.currentTimeMillis();
                    loginUserMap.put("redirectUrl", redirectUrl);
                }
            } catch (Exception e) {
                LOGGER.error("### app/login post error={} ###", e);
                return new RespData(RespCode.SYSERROR.getCode(), RespCode.SYSERROR.getMsg());
            }

            return new RespData(RespCode.RESP_SUCCESS.getCode(), RespCode.RESP_SUCCESS.getMsg(), loginUserMap);
        } else {
            return new RespData(RespCode.USER_OR_PASSWORD_ERROR.getCode(), RespCode.USER_OR_PASSWORD_ERROR.getMsg());
        }
    }

    /**
     * 反向校验token
     *
     * @param httpServletRequest
     * @param param
     * @return
     */
    @RequestMapping("token")
    public RespData verifyToken(HttpServletRequest httpServletRequest, @RequestBody String param) {
        LOGGER.info("### token param={} ###", param);
        String appKey = httpServletRequest.getHeader("appKey");
        String token = JSON.parseObject(param).getString("token");
        LOGGER.info("### token token={} ###", token);
        return appService.verifyAppKeyAndToken(appKey, token);
    }

    /**
     * 退出接口
     *
     * @param httpServletRequest
     * @param httpServletResponse
     */
    @RequestMapping("logout")
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        LOGGER.info("### app/logout  ###");
        String redirectUrl = httpServletRequest.getParameter("redirectUrl");
        String token = CookieUtil.getCookieValue(httpServletRequest, "token");
        RespData respData = appService.verifyUrlAndToken(redirectUrl,token);
        //重复退出也认为是成功的
        if (!RespCode.RESP_SUCCESS.getCode().equals(respData.getCode()) && !RespCode.TOKEN_INVALID.getCode().equals(respData.getCode())) {
            httpServletResponse.addHeader("location", getFailHtml(respData.getCode()));
        } else {
            ssoService.ssoLogout(token);
            try {
                String realToken = secretService.decodeText(token);
                cacheService.delTokenEntity(realToken);
                CookieUtil.removeCookie(httpServletResponse, "token");
                httpServletResponse.addHeader("location", "/view/login.html?redirectUrl=" + URLEncoder.encode(redirectUrl, "utf-8"));
            } catch (Exception e) {
                LOGGER.error("### app/logout error={} ###", e);
            }
        }
        httpServletResponse.setStatus(302);
    }

    /**
     * 退出接口
     *
     * @param httpServletRequest
     * @param httpServletResponse
     */
    @RequestMapping("signout")
    public void signout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        LOGGER.info("### app/signout  ###");
        String token = CookieUtil.getCookieValue(httpServletRequest, "token");
        try {
            if (null != token) {
                ssoService.ssoLogout(token);
                String realToken = secretService.decodeText(token);
                cacheService.delTokenEntity(realToken);
                CookieUtil.removeCookie(httpServletResponse, "token");
            }
            httpServletResponse.addHeader("location", "/index/toLogin");
        } catch (Exception e) {
            LOGGER.error("### app/signout error={} ###", e);
        }
        httpServletResponse.setStatus(302);
    }

    private String getFailHtml(Integer code) {
        return "/view/sso-fail.html?code=" + code;
    }

    /**
     * 查询所有启用的应用列表
     *
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Object queryAllApplications(HttpServletRequest httpServletRequest) {
        LOGGER.info("### /app/all ###");
        try {
            return this.ok(this.applicationInfoService.queryApplications());
        } catch (Exception e) {
            LOGGER.error("/app/all error {}", e);
        }

        return this.error(RespCode.SYSERROR.getCode());
    }

    /**
     * 查询登录用户信息
     *
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/loginUserInfo", method = RequestMethod.GET)
    public Object queryLoginUserInfo(HttpServletRequest httpServletRequest) {

        LOGGER.info("### /app/loginUserInfo ###");
        try {
            String token = CookieUtil.getCookieValue(httpServletRequest, "token");
            return ssoService.getLoginUserInfo(token);
        } catch (Exception e) {
            LOGGER.error("/localUser/delete error {}", e);
        }
        return this.error(RespCode.SYSERROR.getCode());
    }

    /**
     * 查询用户手机号或邮箱是否存在
     * @param request
     * @return
     */
    @ResponseBody
    @SuppressWarnings("JavaDoc")
    @RequestMapping(value="/userCheck",method = RequestMethod.POST)
    public RespData getUserInfo(HttpServletRequest request, @RequestBody LocalUser user){
        LOGGER.info("### app/userCheck:tel-{},email-{} ###",user.getTel(),user.getEmail());
       try{
           int count = localUserService.findUserByEmailOrPhone(user);
           if(count == 0){
               //没有查到账号信息，可以注册绑定手机号或者邮箱，但不可以重置密码的发送验证码
               return new RespData(RespCode.USER_NOT_EXIST.getCode(),RespCode.USER_NOT_EXIST.getMsg());
           }else{
               //查到账号信息，用户可以发送验证码，但不可注册再次绑定用户
                return new RespData(RespCode.RESP_SUCCESS.getCode(),RespCode.RESP_SUCCESS.getMsg());
           }
       }catch (Exception e){
           LOGGER.error("### app/userCheck error:{} ###",e.getMessage());
           return new RespData(RespCode.SYSERROR.getCode(),RespCode.SYSERROR.getMsg());
       }
    }

    //给用户发送验证码
    @ResponseBody
    @RequestMapping(value = "/sendUserCode",method = RequestMethod.POST)
    public RespData sendUserCode(HttpServletRequest request, @RequestBody LocalUser user){
        LOGGER.info("### /sendUserCode: email-{},tel-{}",user.getEmail(),user.getTel());
        Map<String,Object> parameterMap = new HashMap<>();
        String ip = getRemoteIP(request);
        UserCode userCode = new UserCode();
        try{
            String email = user.getEmail();
            String phone = user.getTel();
            String code = CodeUtil.getRandom();
            userCode.setEmail(email);
            userCode.setTel(phone);
            userCode.setCode(code);
            String url;
            if(!Objects.equals("", email) && null != email){
                //发送邮箱验证码
                parameterMap.put("tos",email);
                //String content = java.net.URLEncoder.encode(" 国美云短信验证码，120秒内有效，请勿泄露您的校验码！", "UTF-8");
                String emailText = "<div style='text-align: -webkit-left;'>\n" +
                                "        <h2>尊敬的"+email+"用户，您好 :</h2>\n" +
                                "        <h2>您正在操作您的账号信息，请使用下面的验证码，验证码 120秒内有效:</h2>\n" +
                                "        <h1><a style='color: #2aabd2;margin-left: 278px;font-size: larger'>"+code+"</a></h1>\n" +
                                "        <p>注意：此操作可能会修改您的密码、登录邮箱或绑定手机。如非本人操作，请及时登录并修改密码以保证帐户安全 。请保管好您的邮箱，避免账号被他人盗用。</p>\n" +
                                "        <br>\n" +
                                "        <p>此为系统邮件，请勿回复!</p>\n" +
                                "        <p align='right'>国美云团队</p>\n" +
                                "        </div>";
                parameterMap.put("content",java.net.URLEncoder.encode(emailText, "UTF-8"));
                String title = java.net.URLEncoder.encode("【国美云】账号操作提醒", "UTF-8");
                parameterMap.put("subject",title);
                url = emailUrl;
            }else{
                //发送短信验证码
                parameterMap.put("tos",phone);
                String content = java.net.URLEncoder.encode(" 国美云短信验证码，120秒内有效，请勿泄露您的校验码！", "UTF-8");
                parameterMap.put("content",code+content);
                url = smsUrl;
            }
            //获取两分钟前的时间
            LocalDateTime time = TimeUtil.parseTime(TimeUtil.getCurrentDatetime()).minus(2, ChronoUnit.MINUTES);
            UserCodeInfo userCodeInfo = new UserCodeInfo(null,ip,phone,email,time);
            List<UserCodeInfo> userCodeInfoList = userCodeInfoService.selectList(userCodeInfo);
            time = TimeUtil.parseTime(TimeUtil.getCurrentDatetime(TimeUtil.TimeFormat.SHORT_DATE_PATTERN_LINE)+" 00:00:00");
            userCodeInfo.setCreateDate(time);
            userCodeInfo.setIp(null);
            //同一手机号或者邮箱一天内不超过5次
            List<UserCodeInfo> infoList = userCodeInfoService.selectList(userCodeInfo);
            //	同一IP:不超过10次/1天
            userCodeInfo = new UserCodeInfo(null,ip,null,null,time);
            List<UserCodeInfo> ipList = userCodeInfoService.selectList(userCodeInfo);
            //判断用户两分钟内是否发送过验证码
            //相同的 ip+phone | ip+email 两分钟内只发送一次验证码,防止恶意请求
            if(userCodeInfoList != null && userCodeInfoList.size() > 0){
                //发送过，不发送
                return new RespData(RespCode.USER_COKE_MORE.getCode(),RespCode.USER_COKE_MORE.getMsg());
            }else{
                //两分钟内没发送过,判断一天内发送过几次
                if(infoList.size() >= 5 || ipList.size() > 10){
                    return new RespData(RespCode.USER_COKE_UP.getCode(),RespCode.USER_COKE_UP.getMsg());
                }
            }
            userCodeInfoService.insert(new UserCodeInfo(null,ip,phone,email,TimeUtil.parseTime(TimeUtil.getCurrentDatetime())));
            cacheService.addUserCode(userCode,CODE_TIME);
            String result = HttpRequestUtil.doPost(url,parameterMap);
            //String result = HttpClientUtil.sendPost(url,parameterMap);
            RespResult respResult = (RespResult) JsonUtils.Json2Object(result, RespResult.class);
            if(respResult.getSuccess()){
                return new RespData(RespCode.RESP_SUCCESS.getCode(),RespCode.RESP_SUCCESS.getMsg());
            }
        }catch (Exception e){
            LOGGER.error("### /sendUserCode:{}",e);
            return new RespData(RespCode.SYSERROR.getCode(),RespCode.SYSERROR.getMsg());
        }
        return new RespData(RespCode.SYSERROR.getCode(),RespCode.SYSERROR.getMsg());
    }

    /**
     * 验证码的验证
     * @param userCode
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkUserCode",method = RequestMethod.POST)
    public RespData checkUserCode(@RequestBody UserCode userCode){
        LOGGER.info("## checkUserCode:email-{},tel-{},code-{}",userCode.getEmail(),userCode.getTel(),userCode.getCode());
        try{
            boolean exist = cacheService.exist(userCode.getCode());
            if(exist){
                return new RespData(RespCode.RESP_SUCCESS.getCode(),RespCode.RESP_SUCCESS.getMsg());
            }else{
                return new RespData(RespCode.USER_COKE_INVALID.getCode(),RespCode.USER_COKE_INVALID.getMsg());
            }
        }catch (Exception e){
            LOGGER.error("## checkUserCode error:{}",e);
            return new RespData(RespCode.SYSERROR.getCode(),RespCode.SYSERROR.getMsg());
        }
    }

    //检查用户重置的密码是否可用
    @ResponseBody
    @RequestMapping(value="/checkUserPassword",method = RequestMethod.POST)
    public RespData checkUserPassword(@RequestBody LocalUser localUser){
        LOGGER.info("## checkUserPassword: tel-{},email-{}",localUser.getTel(),localUser.getEmail());
        try{
            Integer uid = localUserService.findUserIdByCondition(localUser);
            List<UserPwdHistory> list = userPwdHistoryService.selectList(new UserPwdHistory(null,uid,null,null));
            if(list != null && list.size() > 0){
               Stream<UserPwdHistory> stream = list.stream();
                /*boolean exist = stream.anyMatch(user -> {
                    return user.getPassword().equals(localUser.getPassword());
                });*/
                if(stream.anyMatch(user -> user.getPassword().equals(localUser.getPassword()))){
                    return new RespData(RespCode.USER_PASSWORD_LIMIT.getCode(),RespCode.USER_PASSWORD_LIMIT.getMsg());
                }
            }
        }catch (Exception e){
            LOGGER.error("## checkUserPassword error:{}",e);
            return new RespData(RespCode.SYSERROR.getCode(),RespCode.SYSERROR.getMsg());
        }
        return new RespData(RespCode.RESP_SUCCESS.getCode(),RespCode.RESP_SUCCESS.getMsg());
    }

    //用户重置密码记录
    @ResponseBody
    @RequestMapping(value = "/resetUserPassword",method = RequestMethod.POST)
    public RespData resetUserPassword(HttpServletRequest httpServletRequest,@RequestBody LocalUser user){
        LOGGER.info("## resetUserPassword: tel-{},email-{}",user.getTel(),user.getEmail());
        SessionUser sessionUser = this.getSessionUser(httpServletRequest);
        Long userId = sessionUser == null ? -1 : sessionUser.getSsoUserId();
        try{
            Integer uid = localUserService.findUserIdByCondition(user);
            userPwdHistoryService.insert(new UserPwdHistory(null,uid,user.getPassword(),TimeUtil.parseTime(TimeUtil.getCurrentDatetime())));
            //修改用户密码
            LocalUser localUser = new LocalUser();
            localUser.setId(uid);
            localUser.setPassword(user.getPassword());
            user.setUpdateUserId(userId.intValue());
            localUserService.update(localUser);
            String email = user.getEmail();
            String username;
            if(email != null && !Objects.equals(email, "")){
                localUserService.updateUserEmailStatus(email);
                username = email;
            }else{
                username = user.getTel();
            }
            localUser = localUserService.selectUserWithUsernameOrTelOrEmail(username);
            userErrorCountService.deleteBeforeRecord(localUser.getUuid());
        }catch (Exception e){
            LOGGER.error("## resetUserPassword error :{}",e);
            return new RespData(RespCode.SYSERROR.getCode(),RespCode.SYSERROR.getMsg());
        }
        return new RespData(RespCode.RESP_SUCCESS.getCode(),RespCode.RESP_SUCCESS.getMsg());
    }

    //用户名非法字符校验和数据库查重校验
    @ResponseBody
    @RequestMapping(value = "/checkUserName",method = RequestMethod.POST)
    public RespData checkUserName(@RequestBody LocalUser user){
        LOGGER.info("## checkUserName: username-{}",user.getUserName());
        String userName = user.getUserName();
        try{
            SensitivewordFilter sensitivewordFilter = new SensitivewordFilter();
            boolean exist = sensitivewordFilter.isContaintSensitiveWord(userName,SensitivewordFilter.maxMatchType);
            if(exist){
                return new RespData(RespCode.USER_NAME_ILLEGAL.getCode(),RespCode.USER_NAME_ILLEGAL.getMsg());
            }else{
                //查询数据库是否已存在
                LocalUser localUser = localUserService.findByUserName(userName);
                //存在
                if(localUser != null){
                    return new RespData(RespCode.USER_NAME_EXIST.getCode(),RespCode.USER_NAME_EXIST.getMsg());
                }
            }
        }catch (Exception e){
            LOGGER.error("## checkUserName error :{}",e);
            return new RespData(RespCode.SYSERROR.getCode(),RespCode.SYSERROR.getMsg());
        }
        return new RespData(RespCode.RESP_SUCCESS.getCode(),RespCode.RESP_SUCCESS.getMsg());
    }

    //用户注册
    @ResponseBody
    @RequestMapping(value = "/userRegister",method = RequestMethod.POST)
    public RespData userRegister(HttpServletRequest httpServletRequest, @RequestBody LocalUser user){
        LOGGER.info("## userRegister: userName-{},tel-{}",user.getUserName(),user.getTel());
        SessionUser sessionUser = this.getSessionUser(httpServletRequest);
        Long userId = sessionUser == null ? -1 : sessionUser.getSsoUserId();
        Map<String, Object> loginUserMap = new HashMap<>();
        try{
            //首先判断当天注册用户是否达到 500个
            LocalDateTime time = TimeUtil.parseTime(TimeUtil.getCurrentDatetime(TimeUtil.TimeFormat.SHORT_DATE_PATTERN_LINE)+" 00:00:00");
            int count = localUserService.selectRegisterUserCount(time);
            if(count < 500){
                String uuid = UUID.randomUUID().toString().replace("-", "");
                user.setUuid(uuid);
                user.setCreateUserId(userId.intValue());
                localUserService.save(user);
                loginUserMap.put("userId",uuid);
                LocalUser localUser = localUserService.selectUserWithUUID(uuid);
                userPwdHistoryService.insert(new UserPwdHistory(null,localUser.getId(),user.getPassword(),LocalDateTime.now()));
                return new RespData(RespCode.RESP_SUCCESS.getCode(),RespCode.RESP_SUCCESS.getMsg(),loginUserMap);
            }else{
                return new RespData(RespCode.USER_REGISTER_UP.getCode(),RespCode.USER_REGISTER_UP.getMsg());
            }
        }catch (Exception e){
            LOGGER.error("## userRegister error :{}",e);
            return new RespData(RespCode.SYSERROR.getCode(),RespCode.SYSERROR.getMsg());
        }
    }

    @RequestMapping(value = "/loginSuccess",method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView loginSuccess(HttpServletResponse httpServletResponse, @Param("userId") String userId,ModelAndView model){
        LOGGER.info("## loginSuccess :userId-{}",userId);
        try{
            LocalUser user = localUserService.selectUserWithUUID(userId);
            model.addObject("userName",user.getUserName());
            model.addObject("post",user.getPost());
            model.addObject("lastLoginDate",user.getLastLoginDate());
            CookieUtil.setCookie(httpServletResponse, "userId", userId, MAX_AGE);
            model.setViewName("index");
        }catch (Exception e){
            LOGGER.error("## loginSuccess error : {}",e);
        }
        return model;
    }

    @ResponseBody
    @RequestMapping(value = "/getUserInfo",method = RequestMethod.POST)
    public RespData getUserInfo(@RequestBody LocalUser localUser){
        LOGGER.info("## getUserInfo : userId-{}",localUser.getUuid());
        Map<String, Object> loginUserMap = new HashMap<>();
        try{
            LocalUser user = localUserService.selectUserWithUUID(localUser.getUuid());
            loginUserMap.put("userName",user.getUserName());
            loginUserMap.put("post",user.getPost());
            loginUserMap.put("lastLoginDate",user.getLastLoginDate());
        }catch (Exception e){
            LOGGER.error("## getUserInfo error :{}",e);
        }
        return new RespData(RespCode.RESP_SUCCESS.getCode(),RespCode.RESP_SUCCESS.getMsg(),loginUserMap);
    }

}
