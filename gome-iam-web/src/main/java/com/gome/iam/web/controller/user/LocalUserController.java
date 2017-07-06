package com.gome.iam.web.controller.user;

import com.gome.iam.common.controller.BaseController;
import com.gome.iam.common.util.CSVUtil;
import com.gome.iam.common.util.TimeUtil;
import com.gome.iam.domain.base.SessionUser;
import com.gome.iam.domain.constant.RespCode;
import com.gome.iam.domain.response.PageData;
import com.gome.iam.domain.user.LocalUser;
import com.gome.iam.domain.user.LocalUserVo;
import com.gome.iam.domain.user.UserPwdHistory;
import com.gome.iam.service.user.LocalUserService;
import com.gome.iam.service.user.UserErrorCountService;
import com.gome.iam.service.user.UserPwdHistoryService;
import com.gome.iam.web.controller.api.GomeAppController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author yintongjiang
 * @params
 * @since 2016/11/7
 */
@RequestMapping("/localUser")
@RestController
public class LocalUserController extends BaseController {
    private final static Logger LOGGER = LoggerFactory.getLogger(GomeAppController.class);
    @Autowired
    private LocalUserService localUserService;
    private static final String FILE_NAME = "本地用户导入模板.csv";
    private static final String[] TITLE = {"userName", "password", "realName", "sex", "post", "type", "company"};
    @Autowired
    private UserPwdHistoryService userPwdHistoryService;
    @Autowired
    private UserErrorCountService userErrorCountService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Object list(HttpServletRequest httpServletRequest,LocalUserVo localUserVo) {
        SessionUser sessionUser = this.getSessionUser(httpServletRequest);
        String LoginName = sessionUser == null ? "nologin" : sessionUser.getUserName();
        LOGGER.info("### /localUser/list user {} ### localUserVo {}", LoginName,localUserVo);
        try {
            PageData<LocalUser> pageData = this.localUserService.findByPage(localUserVo);
            return this.ok(pageData);
        } catch (Exception e) {
            LOGGER.error("/localUser/list error {}", e);
        }

        return this.error(RespCode.SYSERROR.getCode());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object add(HttpServletRequest httpServletRequest,
                      @RequestBody LocalUser localUser) {

        SessionUser sessionUser = this.getSessionUser(httpServletRequest);
        String userName = sessionUser == null ? "nologin" : sessionUser.getUserName();
        Long userId = sessionUser == null ? -1 : sessionUser.getSsoUserId();
        LOGGER.info("### /localUser/add user {} ###", userName);
        try {
            LocalUser user = localUserService.findByEmail(localUser.getEmail());
            if(null != user){
                return this.error(RespCode.USER_EMAIL_EXIST.getCode());
            }
            LocalUser localUserOri = localUserService.findByUserName(localUser.getUserName());
            if (null != localUserOri) {
                return this.error(RespCode.USER_NAME_EXIST.getCode());
            }
            String uuid = UUID.randomUUID().toString().replace("-", "");
            localUser.setUuid(uuid);
            localUser.setCreateUserId(userId.intValue());
            localUserService.save(localUser);
            LocalUser localUsers = localUserService.selectUserWithUUID(uuid);
            userPwdHistoryService.insert(new UserPwdHistory(null,localUsers.getId(),localUser.getPassword(), LocalDateTime.now()));
            return this.ok(localUsers.getId());
        } catch (Exception e) {
            LOGGER.error("/localUser/add error {}", e);
        }

        return this.error(RespCode.SYSERROR.getCode());
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public Object modify(HttpServletRequest httpServletRequest,
                         @RequestBody LocalUser localUser) {

        SessionUser sessionUser = this.getSessionUser(httpServletRequest);
        String userName = sessionUser == null ? "nologin" : sessionUser.getUserName();
        Long userId = sessionUser == null ? -1 : sessionUser.getSsoUserId();
        LOGGER.info("### /localUser/modify user {} ###", userName);
        try {
            localUser.setUpdateUserId(userId.intValue());
            this.localUserService.update(localUser);
            return this.ok(localUser.getId());
        } catch (Exception e) {
            LOGGER.error("/localUser/modify error {}", e);
        }

        return this.error(RespCode.SYSERROR.getCode());
    }

    @RequestMapping(value = "/resetPwd", method = RequestMethod.POST)
    public Object resetPwd(HttpServletRequest httpServletRequest,
                           @RequestBody LocalUser localUser) {

        SessionUser sessionUser = this.getSessionUser(httpServletRequest);
        String userName = sessionUser == null ? "nologin" : sessionUser.getUserName();
        Long userId = sessionUser == null ? -1 : sessionUser.getSsoUserId();
        LOGGER.info("### /localUser/resetPwd user {} ###", userName);
        try {
            localUser.setUpdateUserId(userId.intValue());
            //this.localUserService.resetPwd(localUser);
            localUserService.update(localUser);
            userPwdHistoryService.insert(new UserPwdHistory(null,localUser.getId(),localUser.getPassword(), TimeUtil.parseTime(TimeUtil.getCurrentDatetime())));
            localUser = localUserService.findById(localUser.getId());
            userErrorCountService.deleteBeforeRecord(localUser.getUuid());
            return this.ok(localUser.getId());
        } catch (Exception e) {
            LOGGER.error("/localUser/resetPwd error {}", e);
        }

        return this.error(RespCode.RESET_PASSWORD_FAIL.getCode());
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public Object delete(HttpServletRequest httpServletRequest,
                         @RequestBody LocalUser localUser) {

        SessionUser sessionUser = this.getSessionUser(httpServletRequest);
        String userName = sessionUser == null ? "nologin" : sessionUser.getUserName();
        LOGGER.info("### /localUser/delete user {} ###", userName);
        try {
            Integer id = localUser.getId();
            this.localUserService.deleteById(id);
            localUser = localUserService.findById(id);
            userPwdHistoryService.delete(new UserPwdHistory(id,null,null,null));
            userErrorCountService.deleteBeforeRecord(localUser.getUuid());
            return this.ok(localUser.getId());
        } catch (Exception e) {
            LOGGER.error("/localUser/delete error {}", e);
        }

        return this.error(RespCode.SYSERROR.getCode());
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Object showApplication(HttpServletRequest httpServletRequest,
                                  @RequestParam Integer id) {

        SessionUser sessionUser = this.getSessionUser(httpServletRequest);
        String userName = sessionUser == null ? "nologin" : sessionUser.getUserName();
        LOGGER.info("### /localUser/get user {} ###", userName);
        try {
            LocalUser localUser = this.localUserService.findById(id);
            return this.ok(localUser);
        } catch (Exception e) {
            LOGGER.error("/localUser/get error {}", e);
        }

        return this.error(RespCode.SYSERROR.getCode());
    }

    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    public Object updateStatus(HttpServletRequest httpServletRequest,
                               @RequestBody List<LocalUser> localUserList) {

        SessionUser sessionUser = this.getSessionUser(httpServletRequest);
        String userName = sessionUser == null ? "nologin" : sessionUser.getUserName();
        Long userId = sessionUser == null ? -1 : sessionUser.getSsoUserId();
        LOGGER.info("### /localUser/updateStatus user {} ###", userName);
        try {
            for (LocalUser localUser : localUserList) {
                localUser.setUpdateUserId(userId.intValue());
                this.localUserService.update(localUser);
            }
            return this.ok();
        } catch (Exception e) {
            LOGGER.error("/localUser/updateStatus error {}", e);
        }

        return this.error(RespCode.SYSERROR.getCode());
    }

    @RequestMapping(value = "/genUserName", method = RequestMethod.POST)
    public Object genUserName(HttpServletRequest httpServletRequest,
                              @RequestBody LocalUser localUser) {
        LOGGER.info("### /localUser/genUserName  ###");
        try {
            String userName = generateUserName(localUser.getEmail());
            List<LocalUser> localUserList = localUserService.findLikeUser(userName);
            if (null != localUserList && !localUserList.isEmpty()) {
                LocalUser maxLocalUser = localUserList.get(0);
                String maxUserName = maxLocalUser.getUserName();
                String num = maxUserName.replace("_" + userName, "").replace("epi", "");
                if (!"".equals(num)) {
                    userName = "epi" + (Integer.valueOf(num) + 1) + "_" + userName;
                } else {
                    userName = "epi1_" + userName;
                }
            }else{
                userName = "epi_" + userName;
            }
            return this.ok(userName);
        } catch (Exception e) {
            LOGGER.error("/localUser/genUserName error {}", e);
        }

        return this.error(RespCode.SYSERROR.getCode());
    }

    @RequestMapping("/downloadTemplate")
    @ResponseBody
    public void downloadTemplate(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        SessionUser sessionUser = this.getSessionUser(httpServletRequest);
        String userName = sessionUser == null ? "nologin" : sessionUser.getUserName();
        LOGGER.info("### /localUser/downloadTemplate/ user {} ###", userName);
        byte[] data = getBytes(this.getClass().getResource("/template/").getPath() + FILE_NAME);
        response.reset();
        response.setContentType("application/x-download");
        try {
            response.setHeader("Content-Disposition", "attachment;filename="
                    + new String(FILE_NAME.getBytes("UTF-8"), "iso-8859-1"));
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            LOGGER.error("/localUser/downloadTemplate/ error {}", e);
        }

    }

    @RequestMapping("/importCSV")
    @ResponseBody
    public Object importCustomer(@RequestParam MultipartFile selectCSV, HttpServletRequest request, HttpServletResponse response) {
        try {
            List<Map<String, String>> listMap = CSVUtil.importCSV(selectCSV.getInputStream(), TITLE);
        } catch (Exception e) {
        }
        return null;
    }

    private String generateUserName(String email) {
        return email.substring(0, email.indexOf("@"));
    }

    private Map<String, LocalUser> transLocalUser() {
        Map<String, LocalUser> map = new HashMap<>();
        for (LocalUser localUser : localUserService.findAllUser()) {
            map.put(localUser.getUserName(), localUser);
        }
        return map;
    }

    private byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
}
