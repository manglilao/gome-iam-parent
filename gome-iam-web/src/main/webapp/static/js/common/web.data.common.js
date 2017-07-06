/**
 * Created by tianyuliang on 2016/10/26.
 */


/**
 * 后台管理系统-退出接口
 */
function handler_logout() {
    ajaxDataController(globalApi("web.logout"), {}, null, function (data) {
        if (data && data.code === 0) {
            window.location.href = "/web/toLogin";
        } else {
            $("#logout").attr("data-content", "退出失败.");
            $("#logout").popover("toggle");
        }
    }, true, "get");
}

/**
 * 页面右上角，展示用户名（除了登陆页、注册页之外，其他所有页面均需要调用）
 */
function handler_query_user() {
    ajaxDataController(globalApi("web.user.query"), {}, null, function (data) {
        if (data && data.code === 0) {
            if (data.data && data.data.userName) {
                $("#user-name").html(data.data.userName);
                $("#logout").attr("user-name", data.data.userName);
                $("#logout").attr("user-id", data.data.ssoUserId);
            } else {
                window.location.href = "/web/toLogin";
            }
        } else {
            read_logined_user();
        }
    }, true, "get");
}


/**
 * 从cookies中读取已登陆的用户名
 */
function read_logined_user() {
    try {
        $("#user-name").html($.cookie("web-user") || "");
        $("#logout").attr("user-name", $.cookie("web-user") || "");
    } catch (e) {
    }
}

/**
 * 后台管理登陆页-读取cookies
 */
function read_cookie_user() {
    try {
        $("#txt_user_name").val($.cookie("web-user") || "");
    } catch (e) {
    }
}

/**
 * 后台管理登陆页-设置cookies
 */
function set_cookie_login(cookieValue) {
    try {
        $.cookie("web-user", cookieValue, {path: "/"});
    } catch (e) {
    }
}

/**
 * SSO登陆
 */
function sso_login() {
    if (!validate_login_param()) {
        return;
    }
    //handler_appKey_redirectUrl();
    handler_sso_login();
}

/**
 * 如果submit提交，登录成功，服务端302跳转到app应用的页面，会遭遇跨域问题、cookies无法写入问题 <br/>
 * 因此改用ajax提交参数，登陆成功后由页面自行跳转
 */
function handler_sso_login() {
    var param = {
        "userName": $.trim($("#txt_user_name").val()),
        //"password": $.trim($("#txt_password").val()),
        "password": CryptoJS.MD5($.trim($("#txt_password").val())).toString(),
        "redirectUrl": $.trim($("#redirectUrl").val()),
        "appKey": $.trim($("#appKey").val())
    };
    ajaxDataController(globalApi("ssoApi.login"), JSON.stringify(param), null, function (data) {
        if (data && data.code === 0) {
            if (data.data && data.data.redirectUrl && data.data.redirectUrl.length > 0) {
                gotoHref(data.data.redirectUrl);
            } else {
                /*handler_sso_userinfo(data.data);
                $("#divLoginForm").addClass("sso-div-hidden");
                $("#divAppForm").removeClass("sso-div-hidden");*/
                window.location.href = "/app/loginSuccess?userId="+data.data.userId;
            }
        } else {
            var warning_message = "登录名或密码不正确";
            if (data && data.msg) {
                warning_message = data.msg;
            }
            tipsLoginMsg(warning_message, "#txt_user_name");
            return false;
        }
    }, true, "post");
}

function handler_sso_logout() {
    ajaxDataController(globalApi("ssoApi.login.signout"), null, null, function (data) {
        // 调用成功，服务端302重定向到login.html页
    }, true, "post");
}

function validate_login_param() {
    var userName = $.trim($("#txt_user_name").val());
    var password = $.trim($("#txt_password").val());
    if (userName.length == 0) {
        $("#txt_user_name").focus();
        tipsLoginMsg("请输入用户名", "#txt_user_name");
        return false;
    }
    if (password.length == 0) {
        $("#txt_password").focus();
        tipsLoginMsg("请输入密码", "#txt_password");
        return false;
    }
    return true;
}

function admin_login() {
    if (!validate_login_param()) {
        return;
    }
    var param = {
        "userName": $.trim($("#txt_user_name").val()),
        "password": CryptoJS.MD5($.trim($("#txt_password").val())).toString()
    };
    ajaxDataController(globalApi("web.login"), JSON.stringify(param), null, function (data) {
        if (data && data.code === 0) {
            set_cookie_login(data.data.userName);
            //gotoHref("/web/success?userId="+data.data.sysUserId);
            gotoHref("/web/appList");
        } else {
            var warning_message = "用户名或密码错误.";
            if (data && data.msg) {
                warning_message = data.msg;
            }
            errorMsg(warning_message);
        }
    }, true, "post");
}


/***
 * 兼任域名直接访问的场景，取消redirectUrl和appKey参数空值校验  2016/11/16 Add by tianyuliang
 */
function handler_appKey_redirectUrl() {
    var redirectUrl = getUrlParameter("redirectUrl");
    $("#redirectUrl").val(redirectUrl || "");
    var appKey = getUrlParameter("appKey" || "");
    $("#appKey").val(appKey);
    myConsoleLog("appKey=" + $("#appKey").val() + ",redirectUrl=" + $("#redirectUrl").val());
}
/**
 * 查询SSO登陆用户的信息
 */
function query_sso_userinfo() {
    ajaxDataController(globalApi("ssoApi.user.query"), null, null, function (data) {
        if (data && data.code === 0 && data.data) {
            handler_sso_userinfo(data.data);
            $("#divLoginForm").addClass("sso-div-hidden");
            $("#divAppForm").removeClass("sso-div-hidden");
        } else {
            $("#divLoginForm").removeClass("sso-div-hidden");
        }
    }, true, "get");
}

/**
 * 加载登陆页支持SSO的应用列表
 */
function load_sso_app_list() {
    ajaxDataController(globalApi("ssoApi.app.all"), {}, null, function (data) {
        if (data && data.code === 0 && data.data.length > 0) {
            handler_app_div(data.data);
        } else {
            errorMsg("获取应用列表异常. msg=" + data.msg);
        }
    }, true, "get");
}

/**
 * 登陆成功，设置用户名、上次登陆，职位等信息
 */
function handler_sso_userinfo(bodyData) {
    $(".logo-title").addClass("sso-div-hidden");
    $(".zxcf_nav_r").removeClass("sso-div-hidden");
    $("#user_post").attr('data-content', bodyData.post || "");
    $("#last_login_time").attr('data-content', bodyData.lastLoginDate);
    $("#user_name").html(bodyData.userName);

    // $("#user_post").html(bodyData.post || "");
    // $("#last_login_time").html(bodyData.lastLoginDate);
    // $("#user_name").html(bodyData.userName);
}

function handler_app_div(bodyData) {
    var divHtml = '';
    var isNewLine = false;
    var count = 0;
    $.each(bodyData, function (index, el) {
        count += 1;
        isNewLine = count % 6 == 0 ? true : false;
        divHtml += build_div_appInfo(bodyData[index].appUrl, bodyData[index].appName, isNewLine);
    });
    $("#divAppDetail").empty();
    $("#divAppDetail").append(divHtml);
}

/**
 * 构建SSO应用的按钮样式
 */
function build_div_appInfo(linkUrl, value, isNewLine) {
    var divAppHtml = ''
        + '<div class="col-lg-3 col-md-6"><div class="panel panel-green"><div class="panel-heading"><div class="row"><div class="col-xs-3"><i class="fa fa-eye fa-5x"></i></div><div class="col-xs-9 text-right">'
        + '<div>'+ value +'</div></div></div></div>'
        + '<a href="' + linkUrl + '"><div class="panel-footer"><span class="pull-left">View Details</span><span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span><div class="clearfix"></div>'
        + '</div></a></div></div>';
    // if (isNewLine === true) {
    //     divAppHtml += '<br/><br/><br/>';
    // }
    return divAppHtml;
}

function handler_add_app() {
    var param = new Object();
    param.appName = $.trim($("#txt_app_name").val());
    param.appUrl = $.trim($("#txt_app_link").val()) || "";
    param.domain = $.trim($("#txt_app_domain").val());
    param.expiredApi = $.trim($("#txt_app_token_expired").val());
    param.delUserApi = $.trim($("#txt_user_delete_notify").val());
    var ipHtml = $.trim($("#txt_app_ip_list").val());
    var ips = ipHtml.replace(/\s/g, ",");
    $("#hsIps").val(ips);
    param.ip = ips;
    myConsoleLog("ip list =" + param.ip);

    ajaxDataController(globalApi("api.app.add"), JSON.stringify(param), null, function (data) {
        if (data && data.code === 0 && data.data > 0) {
            successGotoUrl("应该配置成功.", "app.list.html");
        } else {
            errorMsg("操作失败. " + data.msg);
        }
    }, true, "post");
}
function update_app_info() {
    var param = {};
    param.appId = $.trim($("#txt_app_id").val());
    param.appName = $.trim($("#txt_app_name").val());
    param.appUrl = $.trim($("#txt_app_link").val()) || "";
    param.domain = $.trim($("#txt_app_domain").val());
    param.expiredApi = $.trim($("#txt_app_token_expired").val());
    param.delUserApi = $.trim($("#txt_user_delete_notify").val());
    var ipHtml = $.trim($("#txt_app_ip_list").val());
    var ips = ipHtml.replace(/\s/g, ",");
    $("#hsIps").val(ips);
    param.ip = ips;
    myConsoleLog("ip list =" + param.ip);

    ajaxDataController(globalApi("api.app.update"), JSON.stringify(param), null, function (data) {
        if (data.data > 0) {
            successGotoUrl("修改应用成功.", "/web/appList");
        } else {
            errorMsg("修改应用失败. 原因是: " + data.msg);
        }
    }, true, "post");
}

function handler_query_app() {
    //var id = getUrlParameter("appId");
    var id = $("#txt_app_id").val();
    if (!id || isNaN(Number(id)) || Number(id) <= 0) {
        errorGotoUrl("URL需要包括appId参数.", "/web/appList");
    }
    var app_update_api = globalApi("api.app.get") + "&appId=" + Number(id);
    ajaxDataController(app_update_api, {}, null, function (data) {
        if (data.data) {
            $("#txt_app_id").val(data.data.appId);
            $("#txt_app_name").val(data.data.appName);
            $("#txt_app_link").val(data.data.appUrl);
            $("#txt_app_domain").val(data.data.domain);
            var ipHtml = "";
            var ips = data.data.ip.split(",");
            $.each(ips, function (index, el) {
                ipHtml += ips[index] + "\n";
            });
            $("#txt_app_ip_list").val(ipHtml);
            $("#txt_app_token_expired").val(data.data.expiredApi);
            $("#txt_user_delete_notify").val(data.data.delUserApi);
            $("#txt_app_key").val(data.data.appKey);
        } else {
            errorGotoUrl("获取appId参数失败.", "app.list.html");
        }
    }, true, "get");
}


function update_user_info() {
    var param = {};
    param.userName = $.trim($("#user_name").val()) || "";
    param.password = $.trim($("#password").val()) || "";
    param.realName = $.trim($("#real_name").val()) || "";
    param.post = $.trim($("#user_post").val()) || "";
    param.company = $.trim($("#company").val()) || "";
    param.type = $("input[name='user-type']:checked").val();
    param.sex = $("input[name='gender']:checked").val();

    ajaxDataController(globalApi("api.user.add"), JSON.stringify(param), null, function (data) {
        if (data && data.code === 0 && data.data > 0) {
            successGotoUrl("新增用户成功.", "/web/userList");
        } else {
            tipsUserIdMsg(data.msg, "#user_name");
            $("#user_name").focus();
        }
    }, true, "post");
}

function validate_app_param() {
    var appName = $.trim($("#txt_app_name").val());
    var appLink = $.trim($("#txt_app_link").val());
    var appDomain = $.trim($("#txt_app_domain").val());
    //var expiredApi = $.trim($("#txt_app_token_expired").val());
    var ipList = $.trim($("#txt_app_ip_list").val());
    if (appName.length == 0) {
        $("#txt_app_name").focus();
        tipsInputParamMsg("必填项", "#txt_app_name");
        return false;
    }

    if (appLink.length == 0) {
        $("#txt_app_link").focus();
        tipsInputParamMsg("必填项", "#txt_app_link");
        return false;
    }
    if (appLink.substr(0, 4) !== "http" && appLink.substr(0, 5) !== "https") {
        $("#txt_app_link").focus();
        tipsInputParamMsg("主页地址带有Http前缀", "#txt_app_link");
        return false;
    }

    if (appDomain.length == 0) {
        $("#txt_app_domain").focus();
        tipsInputParamMsg("必填项", "#txt_app_domain");
        return false;
    }

    if (ipList.length == 0) {
        $("#txt_app_ip_list").focus();
        tipsInputParamMsg("必填项", "#txt_app_ip_list");
        return false;
    }
    // 匹配IP
    if (ipList.length > 0) {
        var ip = ipList.replace(/\s/g, ",");
        var ips = ip.split(",");
        if (ips.length == 0) {
            tipsInputParamMsg("必填项", "#txt_app_ip_list");
            return false;
        }
        var flag = true, rows = 0;
        $.each(ips, function (i, el) {
            var tmp = match_ip(el);
            myConsoleLog("el=" + el + ", tmp=" + tmp)
            if (match_ip(el) === false) {
                flag = false;
                rows = i + 1;
                return false; // ==> break
            }
        });
        if (!flag) {
            tipsInputParamMsg("第" + rows + "行IP地址无效", "#txt_app_ip_list");
            return false;
        }
    }
    return true;
}

/**
 * 匹配单个IP
 * @param ipValue
 * @returns {true:有效IP,  false:无效IP}
 */
function match_ip(ipValue) {
    var regex_ip = new RegExp("^(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})(\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})){3}$");
    //匹配 10.128.31.103
    var result = regex_ip.test(ipValue);
    if (!result) {
        var tmp = ipValue.split("/");
        if (tmp.length != 2) {
            return false;
        }
        //匹配 10.128.31.103/25
        if (!regex_ip.test(tmp[0]) || isNaN(Number(tmp[1])) || Number(tmp[1]) <= 0 || Number(tmp[1]) > 32) {
            return false;
        }
    }
    return true;
}


function validate_user_param() {
    var userName = $.trim($("#user_name").val());
    var realName = $.trim($("#real_name").val());
    var telphone = $.trim($("#tel_phone").val());
    var userPost = $.trim($("#user_post").val());
    var company = $.trim($("#company").val());

    if (userName.length == 0) {
        $("#user_name").focus();
        tipsInputParamMsg("必填项", "#user_name");
        return false;
    }

    if (realName.length == 0) {
        $("#real_name").focus();
        tipsInputParamMsg("必填项", "#real_name");
        return false;
    }

    if (telphone.length !== 11 || !match_phone(telphone)) {
        $("#tel_phone").focus();
        tipsInputParamMsg("无效手机号", "#tel_phone");
        return false;
    }

    if (userPost.length == 0) {
        $("#user_post").focus();
        tipsInputParamMsg("必填项", "#user_post");
        return false;
    }

    if (company.length == 0) {
        $("#company").focus();
        tipsInputParamMsg("必填项", "#company");
        return false;
    }
    return true;
}

function validate_register_param() {
    var user_email = $.trim($("#user_email").val());
    var password = $.trim($("#password").val());
    var retry_password = $.trim($("#retry_password").val());
    var realName = $.trim($("#real_name").val());
    var userPost = $.trim($("#user_post").val());
    var telphone = $.trim($("#tel_phone").val());
    var company = $.trim($("#company").val());


    if (user_email.length == 0 || !match_email(user_email)) {
        // $("#user_email").focus();
        tipsInputParamMsg("无效邮箱", "#user_email");
        return false;
    }

    if (password.length > 16 || password.length < 6) {
        $("#password").focus();
        tipsInputParamMsg("密码长度6-16位", "#password");
        return false;
    }
    if (!match_password(password)) {
        $("#password").focus();
        tipsInputParamMsg("密码不符合规则", "#password");
        return false;
    }

    if (retry_password !== password) {
        $("#password").focus();
        tipsInputParamMsg("两次密码不一致", "#password");
        return false;
    }

    if (realName.length == 0) {
        $("#real_name").focus();
        tipsInputParamMsg("必填项", "#real_name");
        return false;
    }

    if (telphone.length !== 11 || !match_phone(telphone)) {
        $("#tel_phone").focus();
        tipsInputParamMsg("无效手机号", "#tel_phone");
        return false;
    }

    if (userPost.length == 0) {
        $("#user_post").focus();
        tipsInputParamMsg("必填项", "#user_post");
        return false;
    }

    if (company.length == 0) {
        $("#company").focus();
        tipsInputParamMsg("必填项", "#company");
        return false;
    }
    return true;
}

function match_email(value) {
    var regex_email = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
    return regex_email.test(value);
}

function match_password(value) {
    var regex_password = new RegExp("(?!^\\d+$)(?!^[a-zA-Z]+$)(?!^[_#@]+$).{6,16}");
    return regex_password.test(value);
}

function match_phone(value) {
    var regex_phone = new RegExp("^[1][34578][0-9]{9}$");
    return regex_phone.test(value);
}

function user_register() {
    var userName = $.trim($("#user_name").val()) || "";
    if (userName.length == 0) {
        tipsInputParamMsg("请重新校验用户名", "#user_name");
        return;
    }
    var param = new Object();
    param.email = $.trim($("#user_email").val()) || "";
    param.userName = userName;
    param.password = $.trim($("#password").val()) || "";
    param.realName = $.trim($("#real_name").val()) || "";
    param.tel = $.trim($("#tel_phone").val()) || "";
    param.post = $.trim($("#user_post").val()) || "";
    param.company = $.trim($("#company").val()) || "";
    param.type = $("input[name='user-type']:checked").val() || "1";
    param.sex = $("input[name='gender']:checked").val();

    ajaxDataController(globalApi("api.user.add"), JSON.stringify(param), null, function (data) {
        if (data && data.code === 0 && data.data > 0) {
            var title = "注册成功";
            var content = '';
            content += '<div><div>请记住用户名 <span class="input-lg">' + param.userName + '</span></div><br/>'
                + ' <div><a href="login.html" class="btn btn-sm btn-success btn-block text-center">'
                + ' 前&nbsp;&nbsp;往&nbsp;&nbsp;登&nbsp;&nbsp;陆</a></div></div>';
            tipsContent(title, content);
        } else {
            errorMsg(data.msg);
        }
    }, true, "post");
}

/**
 * 根据邮箱构建注册用户名
 * @param value
 * @returns {boolean}
 */
function build_user_name(value) {
    var user_email = $.trim(value);
    if (user_email.length == 0 || !match_email(user_email)) {
        $("#user_email").focus();
        tipsInputParamMsg("无效邮箱", "#user_email");
        return false;
    }
    var param = {"email": user_email};
    ajaxDataController(globalApi("api.user.buildName"), JSON.stringify(param), null, function (data) {
        if (data && data.code === 0 && data.data.length > 0) {
            $("#user_name").val(data.data);
        } else {
            errorMsg(data.msg);
        }
    }, true, "post");
}

/**
 * 删除本地用户有两次交互：
 * confirm交互、ajax-success交互
 * @param target 事件源
 * @param element_name 元素的name属性值
 */
function handler_delete_user(target, element_name) {
    if (target.nodeName === 'BUTTON' && $(target).attr("name") == element_name) {
        var userName = $(target).attr("realName");
        var msg = "确定删除&nbsp;&nbsp;“" + userName + "”&nbsp;&nbsp;的信息？";
        var userId = $(target).attr("userId");
        delete_user_confirm(msg, userId);
    }
}

function delete_user_confirm(msg, userId) {
    confirmMsg(msg, function () {
        delete_user(userId);
    }, function () {
        // nothing to do
    });
}

function delete_user(userId) {
    var param = {"id": userId};
    ajaxDataController(globalApi("api.user.delete"), JSON.stringify(param), null, function (data) {
        if (data && data.code === 0 && data.data > 0) {
            successAndFn("删除成功", function () {
                $("#table_user_list").bootstrapTable("refresh");
            });
        } else {
            errorMsg("删除异常. " + data.msg);
        }
    }, true, "post");
}

/**
 * 重置本地用户的密码有两次交互：
 * prompt交互、ajax-success交互
 * @param target 事件源
 * @param element_name 元素的name值
 */
function handler_reset_password(target, element_name) {
    if (target.nodeName === 'BUTTON' && $(target).attr("name") == element_name) {
        var msg = "请输入姓名是“" + $(target).attr("realName") + "”的新密码.";
        promptMsg(msg, function (new_value) {
            reset_password($(target).attr("userId"), $.trim(new_value));
        });
    }
}

function reset_password(userId, password) {
    var param = {"id": userId, "password": CryptoJS.MD5(password).toString()};
    ajaxDataController(globalApi("api.user.resetPwd"), JSON.stringify(param), null, function (data) {
        if (data && data.code === 0 && data.data > 0) {
            successAndFn("密码重置成功", function () {
                $("#table_user_list").bootstrapTable("refresh");
            });
        } else {
            errorMsg("操作异常. " + data.msg);
        }
    }, true, "post");
}

/***
 * 单个用户，启用/禁用
 * @param target
 * @param element_name
 */
function handler_review_user_single(target, element_name) {
    if (target.nodeName === 'BUTTON' && $(target).attr("name") == element_name) {
        var user_single = [];
        // 2016/11/25 若原状态为1，则新状态值为0； 若原状态为0，则新状态值为1；  api接口需要传递新状态值 Add by tianyuliang
        var new_status = $(target).attr("oldStatus") === "0" ? 1 : 0;
        var param = {"id": $(target).attr("userId"), "status": new_status};
        user_single.push(param);
        handler_review_user(user_single);
    }
}

/***
 * 批量处理用户，启用/禁用
 * @param new_status    1:启用、通过    0:禁用
 */
function handler_review_user_batch(new_status) {
    if (user_id_selections && user_id_selections.length > 0) {
        var user_batch = [];
        $.each(user_id_selections, function (index, userId) {
            var param = {"id": userId, "status": new_status};
            user_batch.push(param);
        });
        handler_review_user(user_batch);
    }
}

function handler_review_user(ajax_param) {
    ajaxDataController(globalApi("api.user.status"), JSON.stringify(ajax_param), null, function (data) {
        if (data && data.code === 0) {
            successAndFn("操作成功", function () {
                $("#table_user_list").bootstrapTable("refresh");
            });
        } else {
            errorMsg("操作异常. " + data.msg);
        }
    }, true, "post");
}

/***
 * 启用、禁用App
 * @param $element
 * @param element_name
 */
function handler_review_app(target, element_name) {
    if (target.nodeName === 'BUTTON' && $(target).attr("name") == element_name) {
        var new_status = $(target).attr("oldStatus") === "0" ? 1 : 0;
        var param = {
            "appId": $(target).attr("appId"),
            "domain": $(target).attr("domain"),
            "appName": $(target).attr("appName"),
            "status": new_status
        };
        ajaxDataController(globalApi("api.app.update"), JSON.stringify(param), null, function (data) {
            if (data && data.code === 0 && data.data > 0) {
                successAndFn("操作成功", function () {
                    $("#table_app_list").bootstrapTable("refresh");
                });
            } else {
                errorMsg("操作异常. " + data.msg);
            }
        }, true, "post");
    }
}


function update_local_user() {
    if (!validate_user_param()) {
        return;
    }

    var param = {};
    param.id = $.trim($("#hdUserId").val()) || "";
    param.userName = $.trim($("#user_name").val()) || "";
    param.realName = $.trim($("#real_name").val()) || "";
    param.tel = $.trim($("#tel_phone").val()) || "";
    param.post = $.trim($("#user_post").val()) || "";
    param.company = $.trim($("#company").val()) || "";
    param.type = $("input[name='user-type']:checked").val();
    param.sex = $("input[name='gender']:checked").val();

    ajaxDataController(globalApi("api.user.update"), JSON.stringify(param), null, function (data) {
        if (data.data > 0) {
            successGotoUrl("修改成功.", "/web/userList");
        } else {
            errorMsg("修改失败. 原因是: " + data.msg);
        }
    }, true, "post");
}

function handler_query_local_user() {
    //var id = getUrlParameter("id");
    var id = $("#hdUserId").val();
    if (!id || isNaN(Number(id)) || Number(id) <= 0) {
        errorGotoUrl("URL需要包括ID参数.", "/web/userList");
    }
    var user_query_api = globalApi("api.user.get") + "&id=" + Number(id);
    ajaxDataController(user_query_api, {}, null, function (data) {
        if (data.data) {
            $("#hdUserId").val(data.data.id);
            $("#user_name").val(data.data.userName);
            $("#user_email").val(data.data.email);
            $("#real_name").val(data.data.realName);
            $("#tel_phone").val(data.data.tel);
            $("#user_post").val(data.data.post);
            $("#company").val(data.data.company);
            $("input[name='user-type'][value='" + data.data.type + "']").attr("checked", true);
            $("input[name='gender'][value='" + data.data.sex + "']").attr("checked", true);
            $("#user_uuid").val(data.data.uuid);
        } else {
            errorGotoUrl("获取ID参数失败.", "/web/userList");
        }
    }, true, "get");
}