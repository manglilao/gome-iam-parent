/**
 * Created by qiaowentao on 2017/6/9.
 */


$(function () {

    showCont();
    $("input[name=confirm]").click(function(){
        showCont();
    });

    var redirectUrl = $.trim($("#redirectUrl").val());
    $(".reset-close").click(function () {
        resetPwdToLogin(redirectUrl);
    });

    $("#toRegister").click(function () {
        if(redirectUrl.length == 0){
            window.location.href = contextPath+"/index/toRegister";
        }else{
            window.location.href = contextPath+"/index/toRegister?redirectUrl="+redirectUrl;
        }
    });


    $("#toLogin").click(function () {
        resetPwdToLogin(redirectUrl);
    });

});

function resetPwdToLogin(toUrl) {
    if(toUrl.length == 0){
        window.location.href = contextPath+"/index/toLogin";
    }else{
        window.location.href = contextPath+"/index/toLogin?redirectUrl="+toUrl;
    }
}

var t;

//切换邮箱和手机号注册
function showCont(){
    clearTimeout(t);
    time = 120;
    $(".info_span").hide();
    $("#new_password").val("");
    $("#again_new_password").val("");
    $(".send_code").attr("style","background-color: #33b5d4;");
    switch($("input[name=confirm]:checked").attr("id")){
        case "confirm_email":
            //alert("one");
            $("#input_phone").hide();
            $("#input_phone_code").hide();
            $("#input_email").show();
            $("#input_email_code").show();
            $("#user_phone").val("");
            $("#phone_code").val("");
            $(".send_phone_code").attr({"disabled":false});
            $(".send_phone_code").val("发送验证码");
            $("#phone_code_next").hide();
            $("#email_code_next").show();
            $("#phone_notify").addClass("sso-div-hidden");
            $("#email_notify").removeClass("sso-div-hidden");
            break;
        case "confirm_phone":
            $("#input_email").hide();
            $("#input_email_code").hide();
            $("#input_phone").show();
            $("#input_phone_code").show();
            $("#user_email").val("");
            $("#email_code").val("");
            $(".send_email_code").attr({"disabled":false});
            $(".send_email_code").val("发送验证码");
            $("#email_code_next").hide();
            $("#phone_code_next").show();
            $("#phone_notify").removeClass("sso-div-hidden");
            $("#email_notify").addClass("sso-div-hidden");
            break;
        default:
            break;
    }
}

//后台查询用户输入的信息是否有效
function checkUserInfo(type,info) {
    if(info.length != 0){
        var param = {};
        if(type == 1){
            param.tel = info;
        }else{
            param.email = info;
        }

        ajaxDataController(globalApi("ssoApi.user.check"), JSON.stringify(param), null, function (data) {
            if (data.code == 0) {
                //用户邮箱或者手机号正确,执行下一步：可以发送验证码
                if(type == 1){
                    $("#user_phone").siblings(".info_span").hide().text("");
                }else{
                    $("#user_email").siblings(".info_span").hide().text("");
                }
            } else {
                var msg;
                if(type == 1){
                    msg = "请输入正确的手机号码";
                    //infoFocus(type,msg);
                    $("#user_phone").siblings(".info_span").show().text(msg);
                }else{
                    msg = "请输入正确的邮箱";
                    //infoFocus(type,msg);
                    $("#user_email").siblings(".info_span").show().text(msg);
                }
            }
        }, true, "post");
    }else{
        if(type == 1){
            $("#user_phone").siblings(".info_span").show().text("请输入手机号码");
        }else{
            $("#user_email").siblings(".info_span").show().text("请输入邮箱");
        }
    }
}

function infoFocus(type,msg) {
    if(type == 1){
        $("#user_phone").siblings(".info_span").show().text(msg);
        //$("#user_phone").focus();
    }else{
        $("#user_email").siblings(".info_span").show().text(msg);
        //$("#user_email").focus();
    }
}

function newPasswordFocus(msg,id) {
    $(id).siblings(".info_span").show().text(msg);
    //$(id).focus();
}

var time=120;

//调用后台发送验证码
function sendCode(obj,info,type) {
    var param = {};
    if(type == 1){
        if(info != "" && info != null){
            param.tel = info;
        }else{
            infoFocus(type,"请输入手机号码");
            return false;
        }
    }else{
        if(info != "" && info != null){
            param.email = info;
        }else{
            infoFocus(type,"邮箱地址不能为空");
            return false;
        }
    }

    /*var password = $.trim($("#new_password").val());
    if(password.length == 0){
        $("#new_password").siblings(".info_span").show().text("请输入密码");
        return false;
    }

    var passwordText = $("#new_password").siblings(".info").text();
    if(passwordText.length != 0){
        $("#new_password").siblings(".info_span").show().text(passwordText);
        return false;
    }else{
        $("#new_password").siblings(".info_span").hide().text("");
    }

    testAgainPassword();*/
    //checkUserInfo(type,info);
    var text;
    if(type == 1){
        text = $("#user_phone").siblings(".info_span").text();
    }else{
        text = $("#user_email").siblings(".info_span").text();
    }
    if(text.length != 0){
        infoFocus(type,text);
        return false;
    }

    //调用后台发送验证码
    ajaxDataController(globalApi("ssoApi.send.user.code"), JSON.stringify(param), null, function (data) {
        if (data.code == 0) {
            $("#phone_code").siblings(".info_span").hide().text("");
            $("#email_code").siblings(".info_span").hide().text("");
            //设置按钮倒计时
            settime(obj);
        } else {
            user_code_focus(type,data.msg);
            return false;
        }
    }, true, "post");
}

//点击发送验证码后按钮显示倒计时
function settime(obj) {
    if (time == 0) {
        obj.removeAttribute("disabled");
        obj.setAttribute("style","background-color: #33b5d4;");
        obj.value="发送验证码";
        time = 120;
        return;
    } else {
        obj.setAttribute("disabled", true);
        obj.value="重新发送(" + time + ")";
        obj.setAttribute("style","background-color: #cccccc;");
        time--;
    }
    t = setTimeout(function() {
            settime(obj) }
        ,1000)
}

function user_code_focus(type,msg){
    if(type == 1){
        $("#phone_code").siblings(".info_span").show().text(msg);
        //$("#phone_code").focus();
    }else{
        $("#email_code").siblings(".info_span").show().text(msg);
        //$("#email_code").focus();
    }
}

//验证用户的验证码是否正确
function checkCode(info,code,type) {
    if(code.length != 0){

        var codeText;
        if(type == 1){
            codeText = $("#user_phone").siblings(".info_span").text();
        }else{
            codeText = $("#user_email").siblings(".info_span").text();
        }

        if(codeText.length != 0){
            $("#user_phone").siblings(".info_span").show().text(codeText);
            $("#user_email").siblings(".info_span").show().text(codeText);
            return false;
        }

        var param = {
            "code":code
        };
        if(type == 1){
            param.tel = info;
        }else{
            param.email = info;
        }
        //后台匹配用户输入验证码是否正确
        confirmUserCode(type,param);
    }
}

function confirmUserCode(type,param) {

    ajaxDataController(globalApi("ssoApi.check.user.code"), JSON.stringify(param), null, function (data) {
        if (data.code == 0) {
            //用户邮箱或者手机号验证码正确,执行下一步：可以重置密码
            if (type == 1) {
                $("#phone_code").siblings(".info_span").text("");
            } else {
                $("#email_code").siblings(".info_span").text("");
            }
        } else {
            if(type == 1){
                $("#phone_code").siblings(".info_span").text(data.msg);
            }else{
                $("#email_code").siblings(".info_span").text(data.msg);
            }
            return false;
        }
    }, true, "post");

}

function confirmReset(type) {
    var code;
    var text;
    var codeText;
    var param = {};
    var codeParam = {};
    var passwordParam = {};
    var userParam = {};
    if(type == 1){
        var phone = $.trim($("#user_phone").val());
        codeText = $("#phone_code").siblings(".info_span").text();
        if(phone.length == 0){
            infoFocus(type,"请输入手机号码");
            return false;
        }
        text = $("#user_phone").siblings(".info_span").text();
        code = $.trim($("#phone_code").val());
        param.tel = phone;
        codeParam.tel = phone;
        passwordParam.tel = phone;
        userParam.tel = phone;
    }else{
        var email = $.trim($("#user_email").val());
        codeText = $("#email_code").siblings(".info_span").text();
        if(email.length == 0){
            infoFocus(type,"邮箱不能为空");
            return false;
        }
        text = $("#user_email").siblings(".info_span").text();
        code = $.trim($('#email_code').val());
        param.email = email;
        codeParam.email = email;
        passwordParam.email = email;
        userParam.email = email;
    }

    if(text.length != 0){
        infoFocus(type,text);
        return false;
    }
    ajaxDataController(globalApi("ssoApi.user.check"), JSON.stringify(userParam), null, function (data) {
        if (data.code == 0) {
            //用户邮箱或者手机号正确,执行下一步：可以发送验证码
            if(type == 1){
                $("#user_phone").siblings(".info_span").hide().text("");
            }else{
                $("#user_email").siblings(".info_span").hide().text("");
            }

            resetCheckPassword(passwordParam,code,codeText,codeParam,type,param);

        } else {
            var msg;
            if(type == 1){
                msg = "请输入正确的手机号码";
                //infoFocus(type,msg);
                $("#user_phone").siblings(".info_span").show().text(msg);
            }else{
                msg = "请输入正确的邮箱";
                //infoFocus(type,msg);
                $("#user_email").siblings(".info_span").show().text(msg);
            }
        }
    }, true, "post");



}

//提交时检查密码
function resetCheckPassword(passwordParam,code,codeText,codeParam,type,param) {
    var password = $.trim($("#new_password").val());
    var confirmPassword = $.trim($("#again_new_password").val());

    if(password.length == 0){
        newPasswordFocus("请输入密码","#new_password");
        return false;
    }

    //数据库查看用户输入是否可用
    passwordParam.password = CryptoJS.MD5(password).toString();
    //查询数据库密码历史记录
    ajaxDataController(globalApi("ssoApi.check.user.password"), JSON.stringify(passwordParam), null, function (data) {
        if (data.code == 0) {
            //密码可用：可以重置密码
            $("#new_password").siblings(".info_span").text("");

            if(confirmPassword.length == 0){
                newPasswordFocus("请输入密码","#again_new_password");
                return false;
            }

            if(confirmPassword !== password){
                newPasswordFocus("两次输入的密码不一致，请重新输入","#again_new_password");
                return false;
            }else{
                $("#again_new_password").siblings(".info_span").hide().text("");
            }

            if(codeText.length != 0){
                user_code_focus(type,codeText);
                return false;
            }

            if(code.length == 0){
                user_code_focus(type,"请输入验证码");
                return false;
            }

            codeParam.code = code;

            resetCheckCode(codeParam,type,param,password);

        } else {
            $("#new_password").siblings(".info_span").show().text(data.msg);
            return false;
        }
    }, true, "post");
}

//提交时验证验证码
function resetCheckCode(codeParam,type,param,password) {

    ajaxDataController(globalApi("ssoApi.check.user.code"), JSON.stringify(codeParam), null, function (data) {
        if (data.code == 0) {
            //用户邮箱或者手机号验证码正确,执行下一步：可以重置密码
            if (type == 1) {
                $("#phone_code").siblings(".info_span").text("");
            } else {
                $("#email_code").siblings(".info_span").text("");
            }

            param.password = CryptoJS.MD5(password).toString();
            resetPassword(param);
        } else {
            if(type == 1){
                $("#phone_code").siblings(".info_span").text(data.msg);
            }else{
                $("#email_code").siblings(".info_span").text(data.msg);
            }
            return false;
        }
    }, true, "post");
}

//进行密码的重置
function resetPassword(param) {
    //重置密码成功，进行跳转操作
    ajaxDataController(globalApi("ssoApi.reset.user.password"), JSON.stringify(param), null, function (data) {
        if (data.code == 0) {
            //密码重置成功
            $("#divAppForm").addClass("sso-div-hidden");
            $("#divResetPwdForm").removeClass("sso-div-hidden");
            setInterval("redirectReset()", 1000);
        } else {
            layer.msg(data.msg);
            return false;
        }
    }, true, "post");
}

var num = 1;
function redirectReset(){
    num--;
    if(num<0){
        var redirectUrl = $.trim($("#redirectUrl").val());
        if(redirectUrl.length == 0){
            window.location.href = contextPath+"/index/toLogin";
        }else{
            window.location.href = contextPath+"/index/toLogin?redirectUrl="+redirectUrl;
        }
    }
}

//密码格式验证
function testPassword(password) {
    var msg;
    var id ="#new_password";
    var tel = $("#user_phone").val();
    var email = $("#user_email").val();

    if(password.length == 0){
        msg = "请输入密码";
        $(id).siblings(".info_span").text(msg);
        return false;
    }
    if(password.length < 8 || password.length > 16 || password.indexOf(" ") != -1){
        msg = "密码格式不正确，请重新输入";
        $(id).siblings(".info_span").text(msg);
        return false;
    }

    var checkPassword = /^(?=.*[a-zA-Z0-9].*)(?=.*[a-zA-Z\W].*)(?=.*[0-9\W].*).{8,16}$/;
    if(!checkPassword.test(password)){
        msg = "密码格式不正确，请重新输入";
        $(id).siblings(".info_span").text(msg);
        return false;
    }else{
        $(id).siblings(".info_span").text("");
    }

    if(tel+email.length == 0){
        $("#user_phone").siblings(".info_span").show().text("请填写手机号");
        $("#user_email").siblings(".info_span").show().text("请填写邮箱");
        return false;
    }

    checkControllerPassword(password,tel,email);

}

function checkControllerPassword(password,tel,email) {
    var param = {
        "password":CryptoJS.MD5(password).toString(),
        "tel":tel,
        "email":email
    };
    //查询数据库密码历史记录
    ajaxDataController(globalApi("ssoApi.check.user.password"), JSON.stringify(param), null, function (data) {
        if (data.code == 0) {
            //密码可用：可以重置密码
            $("#new_password").siblings(".info_span").text("");

            var againPassword = $.trim($("#again_new_password").val());
            if(againPassword.length != 0){
                if(password !== againPassword){
                    $("#again_new_password").siblings(".info_span").show().text("两次输入的密码不一致，请重新输入");
                    return false;
                }else{
                    $("#again_new_password").siblings(".info_span").hide().text("");
                }
            }

        } else {
            $("#new_password").siblings(".info_span").show().text(data.msg);
            return false;
        }
    }, true, "post");
}

function testAgainPassword() {
    var password = $.trim($("#new_password").val());
    var againPassword = $.trim($("#again_new_password").val());
    if(againPassword.length == 0){
        $("#again_new_password").siblings(".info_span").show().text("请输入密码");
        return false;
    }
    if(password !== againPassword){
        $("#again_new_password").siblings(".info_span").show().text("两次输入的密码不一致，请重新输入");
        return false;
    }else{
        $("#again_new_password").siblings(".info_span").hide().text("");
    }

}

function password_notify() {
    var password = $.trim($('#new_password').val());
    if(password.length == 0){
        $("#new_password").siblings(".info_span").show().text("8~16位字符:包含数字、字母、符号至少2种");
    }
}