package com.gome.iam.domain.constant;

/**
 * @author yintongjiang
 * @params
 * @since 2016/10/21
 */
public enum RespCode {
    RESP_SUCCESS(0, "成功"),

    APP_KEY_ERROR(100001, "appKey错误"),
    TOKEN_BLANK(100002, "token为空"),
    TOKEN_ERROR(100003, "token错误"),
    TOKEN_DECODE_ERROR(100004, "token解密错误"),
    APP_KEY_BLANK(100005, "appKey为空"),
    APP_KEY_NOT_MATCH(100006, "appKey和url的domain不匹配"),
    REQ_PARAM_ERROR(100007, "请求参数不正确"),
    USER_OR_PASSWORD_ERROR(100008, "用户名或密码错误"),
    TOKEN_INVALID(100009, "token失效"),
    APP_STOPPING(100010, "应用已被禁用"),
    USER_NAME_EXIST(100011, "用户名已存在"),
    USER_NOT_EXIST(100012, "用户名不存在"),
    PLEASE_LOGIN(100013, "请先登录"),
    RESET_PASSWORD_FAIL(100014, "重置密码失败"),
    REDIRECT_URL_INVALID(100015, "redirectUrl无效"),
    USER_EMAIL_EXIST(100016,"该邮箱已被注册"),
    USER_COKE_INVALID(100017,"验证码无效或者已过期"),
    USER_COKE_MORE(100018,"请求太频繁，请稍后再试。"),
    USER_COKE_UP(100019,"验证请求达到上限，请24小时后重试"),
    USER_PASSWORD_LIMIT(100020,"新密码不能与近5次密码相同"),
    USER_NAME_ILLEGAL(100021,"存在非法字符"),
    USER_REGISTER_UP(100022,"注册达到上限，请明天重试"),
    USER_FREEZE(100023,"账户已锁定"),
    USER_FORBIDDEN(100024,"该账户已被禁用"),
    SYSERROR(500000, "系统错误");
    private Integer code;
    private String msg;

    RespCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public static String getMsgByCode(int code) {
        for (RespCode c : RespCode.values()) {
            if (c.getCode() == code) {
                return c.msg;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static void main(String[] args) {
        System.out.println(RespCode.getMsgByCode(500000));
    }
}
