/***
 * 获取全局API接口
 * @param key 接口标识
 * @returns 具体某个API地址
 */
function globalApi(key) {
    var globalConfig = {
        "enblabRootPath": "1",   // 0:启用RootPat, 1:关闭RootPath（例如 http://127.0.0.1:8090/rootPath/view/login.html）

        "ssoApi.domain": "",
        "ssoApi.rootPath": "",
        "ssoApi.login": "/app/login/",
        "ssoApi.loginout": "/app/logout/",
        "ssoApi.verify_token": "/app/verify_token/",
        "ssoApi.app.all": "/app/all/",
        "ssoApi.login.signout":"/app/signout",
        "ssoApi.user.query": "/app/loginUserInfo/",
        "ssoApi.user.check":"/app/userCheck",
        "ssoApi.send.user.code":"/app/sendUserCode",
        "ssoApi.check.user.code":"/app/checkUserCode",
        "ssoApi.check.user.password":"/app/checkUserPassword",
        "ssoApi.reset.user.password":"/app/resetUserPassword",
        "ssoApi.check.user.name":"/app/checkUserName",
        "ssoApi.user.register":"/app/userRegister",
        "ssoApi.user.getUserInfo":"/app/getUserInfo",

        "api.domain": "",
        "api.rootPath": "",
        "api.login": "/sysUser/login/",
        "api.logout": "/sysUser/logout/",

        "api.app.list": "/appinfo/list/",
        "api.app.add": "/appinfo/add/",
        "api.app.get": "/appinfo/get/",
        "api.app.update": "/appinfo/modify/",

        "api.user.query": "/sysUser/loginUser/",
        "api.user.get": "/localUser/get/",
        "api.user.list": "/localUser/list/",
        "api.user.add": "/localUser/add/",   // 对应第三方用户注册接口 "/app/register/",
        "api.user.update": "/localUser/modify/",
        "api.user.resetPwd": "/localUser/resetPwd/",
        "api.user.status": "/localUser/updateStatus/",
        "api.user.delete": "/localUser/delete/",
        "api.user.buildName": "/localUser/genUserName/",

        "web.login":"/web/login",
        "web.user.query": "/web/loginUser",
        "web.user.add": "/web/add",
        "web.logout": "/web/logout"

    };
    var api = globalConfig[key] || "/unknown/"
    return api + "?r=" + Math.random();
}

function globalError(code) {
    var errorlConfig = {
        "100001": "appKey错误",
        "100002": "token为空",
        "100003": "token错误",
        "100004": "token解密错误",
        "100005": "appKey为空",
        "100006": "appKey和url的domain不匹配",
        "100007": "请求参数不正确",
        "100008": "用户名或密码错误",
        "100009": "token失效",
        "100010": "应用未被授权接入，请联系SSO管理员授权。",
        "100015": "redirectUrl无效",
        "500000": "系统错误"
    };
    return errorlConfig[code] || "内部异常";
}