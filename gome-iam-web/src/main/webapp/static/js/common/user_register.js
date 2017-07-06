/**
 * Created by qiaowentao on 2017/6/19.
 */

$(function () {

    $("#btn-user-register").click(function () {

        userRegister();

    });

    var redirectUrl = $.trim($("#redirectUrl").val());

    //立即登录
    $("#atOnceToLogin").click(function () {
        toRedirect(redirectUrl);
    });

    //去登录
    $("#registerToLogin").click(function () {
        toRedirect(redirectUrl);
    });

    $("#serviceAgreement").click(function () {
        if(redirectUrl.length == 0){
            //window.location.href = contextPath+"/index/toServiceAgreement";
            window.open(contextPath+"/index/toServiceAgreement");
        }else{
            //window.location.href = contextPath+"/index/toServiceAgreement?redirectUrl="+redirectUrl;
            window.open(contextPath+"/index/toServiceAgreement?redirectUrl="+redirectUrl);
        }
    });

});

function toRedirect(toUrl) {
    if(toUrl.length == 0){
        window.location.href = contextPath+"/index/toLogin";
    }else{
        window.location.href = contextPath+"/index/toLogin?redirectUrl="+toUrl;
    }
}

function checkUserName(username) {

    if(username.length == 0){
        $("#user_register_names").children(".info_span").show().text("用户名不能为空");
        //$("#user_register_name").focus();
        return false;
    }

    if(username.length < 5 || username.length > 25){
        $("#user_register_names").children(".info_span").show().text("用户名格式不正确，请重新输入");
        //$("#user_register_name").focus();
        return false;
    }

    //var usernameReg = /^\w{5,25}$/;
    //var usernameReg = /^((?=.*\d)(?=.*[A-z_])|(?=.*[A-z])(?=.*_))\w*$/;
    var usernameReg = /^[\da-zA-Z_]+$/;
    if(!usernameReg.test(username)){
        $("#user_register_names").children(".info_span").show().text("用户名存在非法字符");
        //$("#user_register_name").focus();
        return false;
    }else{
        $("#user_register_names").children(".info_span").hide().text("");
    }

    var param = {
        "userName":username
    };

    ajaxDataController(globalApi("ssoApi.check.user.name"), JSON.stringify(param), null, function (data) {
        if(data.code == 0){
            //用户名可以使用
            $("#user_register_names").children(".info_span").hide().text("");
        }else{
            $("#user_register_names").children(".info_span").show().text(data.msg);
            //$("#user_register_name").focus();
            return false;
        }
    }, true, "post");

}

function notify_username() {
    var username = $.trim($("#user_register_name").val());
    if(username.length == 0){
        $("#user_register_names").children(".info_span").show().text("5~25位字符（数字、字母、下划线）,一旦设置成功无法修改");
    }
}

function checkRegisterPassword(password) {
    var id = "#user_register_passwords";
    if(password.length == 0){
        $(id).children(".info_span").show().text("请输入密码");
        //$(id).focus();
        return false;
    }

    var checkPassword = /^(?=.*[a-zA-Z0-9].*)(?=.*[a-zA-Z\W].*)(?=.*[0-9\W].*).{8,16}$/;
    if(!checkPassword.test(password) ||  password.indexOf(" ") != -1){
        $(id).children(".info_span").show().text("密码格式不正确，请重新输入");
        //$(id).focus();
        return false;
    }else{
        $(id).children(".info_span").text("");
    }

    var againPassword = $.trim($("#again_user_register_password").val());
    if(againPassword.length != 0){
        if(againPassword !== password){
            $("#again_user_register_passwords").children(".info_span").show().text("两次输入的密码不一致，请重新输入");
            return false;
        }else{
            $("#again_user_register_passwords").children(".info_span").hide().text("");
        }
    }

}

//再次输入密码验证与第一次输入是否一样
function againCheckRegisterPassword() {
    var password = $.trim($("#user_register_password").val());
    var againPassword = $.trim($("#again_user_register_password").val());
    if(againPassword.length == 0){
        $("#again_user_register_passwords").children(".info_span").show().text("请重新输入密码");
        return false;
    }
    if(password !== againPassword){
        $("#again_user_register_passwords").children(".info_span").show().text("两次输入的密码不一致，请重新输入");
        return false;
    }else{
        $("#again_user_register_passwords").children(".info_span").hide().text("");
    }
}

function notify_pwd() {
    var password = $.trim($("#user_register_password").val());
    if(password.length == 0){
        $("#user_register_passwords").children(".info_span").show().text("8~16位字符:包含数字、字母、符号至少2种");
    }
}

function checkRegisterTel(tel) {
    if(tel.length == 0){
        $("#user_register_tels").children(".info_span").show().text("手机号码不能为空");
        //$("#user_register_tel").focus();
        return false;
    }

    var regex_phone = new RegExp("^[1][34578][0-9]{9}$");
    if(!regex_phone.test(tel)){
        $("#user_register_tels").children(".info_span").show().text("格式不正确，请重新输入");
        //$("#user_register_tel").focus();
        return false;
    }else{
        $("#user_register_tels").children(".info_span").text("");
    }

    var param = {
      "tel":tel
    };

    ajaxDataController(globalApi("ssoApi.user.check"), JSON.stringify(param), null, function (data) {
        if(data.code == 100012){
            //手机号可以注册使用
            $("#user_register_names").children(".info_span").hide().text("");
        }else if(data.code == 0){
            //该手机号已被使用
            $("#user_register_tels").children(".info_span").show().text("手机已注册，请直接登录或者更换号码");
            //$("#user_register_tel").focus();
            return false;
        }else{
            $("#user_register_tels").children(".info_span").show().text(data.msg);
            //$("#user_register_tel").focus();
            return false;
        }
    }, true, "post");

}

var timeCount = 120;

function sendRegisterCode(obj,username,tel) {

    if(username.length == 0){
        $("#user_register_names").children(".info_span").show().text("用户名不能为空");
        //$("#user_register_name").focus();
        return false;
    }

    if(tel.length == 0){
        $("#user_register_tels").children(".info_span").show().text("手机号码不能为空");
        //$("#user_register_tel").focus();
        return false;
    }

    var usernameText = $("#user_register_names").children(".info_span").text();
    if(usernameText.length != 0){
        $("#user_register_names").children(".info_span").show().text(usernameText);
        //$("#user_register_name").focus();
        return false;
    }

    var telText = $("#user_register_tels").children(".info_span").text();
    if(telText.length != 0){
        $("#user_register_tels").children(".info_span").show().text(telText);
        //$("#user_register_tel").focus();
        return false;
    }

    var param = {
        "tel":tel
    };

    //调用后台发送验证码
    ajaxDataController(globalApi("ssoApi.send.user.code"), JSON.stringify(param), null, function (data) {
        if (data.code == 0) {
            $("#user_register_codes").children(".info_span").hide().text("");
            //设置按钮倒计时
            setRegisterTime(obj);
        } else {
            $("#user_register_codes").children(".info_span").show().text(data.msg);
            $("#user_register_code").focus();
            return false;
        }
    }, true, "post");
}

//点击发送验证码后按钮显示倒计时
function setRegisterTime(obj) {
    if (timeCount == 0) {
        obj.removeAttribute("disabled");
        obj.setAttribute("style","background-color: #33b5d4;");
        obj.value="发送验证码";
        timeCount = 120;
        return;
    } else {
        obj.setAttribute("disabled", true);
        obj.value="重新发送(" + timeCount + ")";
        obj.setAttribute("style","background-color: #cccccc;");
        timeCount--;
    }
    t = setTimeout(function() {
            setRegisterTime(obj) }
        ,1000)
}

//验证用户填写的邮箱是否格式正确
function checkEmail(email) {
    if(email.length == 0){
        $("#user_register_emails").children(".info_span").show().text("请输入邮箱");
        return false;
    }
    //var regex_email = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
    var regex_email = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/;

    if(!regex_email.test(email)){
        $("#user_register_emails").children(".info_span").show().text("邮箱格式不正确");
        return false;
    }else{
        $("#user_register_emails").children(".info_span").hide().text("");
    }

    checkControllerEmail(email);

}

function checkControllerEmail(email) {

    var param = {
       "email":email
    };

    ajaxDataController(globalApi("ssoApi.user.check"), JSON.stringify(param), null, function (data) {
        if (data.code == 0) {
            //用户邮箱已被绑定，不可用于注册
            $("#user_register_emails").children(".info_span").show().text("邮箱已注册，请直接登录/更换邮箱地址");
        } else if(data.code == 100012){
            $("#user_register_emails").children(".info_span").hide().text("");

        }else{
            layer.msg(data.msg);
        }
    }, true, "post");
}

function checkRegisterCode(code){
    if(code.length != 0){

        var codeText = $("#user_register_tels").children(".info_span").text();
        if(codeText.length != 0){
            $("#user_register_tels").children(".info_span").show().text(codeText);
            return false;
        }else{
            $("#user_register_tels").children(".info_span").hide().text("");
        }

        var param = {
            "code":code
        };

        ajaxDataController(globalApi("ssoApi.check.user.code"), JSON.stringify(param), null, function (data) {
            if (data.code == 0) {
                //用户手机号验证码正确,可以注册
                $("#user_register_codes").children(".info_span").text("");
            } else {
                $("#user_register_codes").children(".info_span").show().text(data.msg);
                return false;
            }
        }, true, "post");

    }
}

function userRegister() {

    var username = $.trim($("#user_register_name").val());
    if(username.length == 0){
        $("#user_register_names").children(".info_span").show().text("用户名不能为空");
        //$("#user_register_name").focus();
        return false;
    }

    var usernameText = $("#user_register_names").children(".info_span").text();
    if(usernameText.length != 0){
        $("#user_register_names").children(".info_span").show().text(usernameText);
        //$("#user_register_name").focus();
        return false;
    }

    var userRegisterPwd = $.trim($("#user_register_password").val());
    if(userRegisterPwd.length == 0){
        $("#user_register_passwords").children(".info_span").show().text("请输入密码");
        //$("#user_register_password").focus();
        return false;
    }

    var userRegisterPwdText = $("#user_register_passwords").children(".info_span").text();
    if(userRegisterPwdText.length != 0){
        $("#user_register_passwords").children(".info_span").show().text(userRegisterPwdText);
        //$("#user_register_password").focus();
        return false;
    }

    var againUserRegisterPwd = $.trim($("#again_user_register_password").val());
    if(againUserRegisterPwd.length == 0){
        $("#again_user_register_passwords").children(".info_span").show().text("请输入密码");
        //$("#again_user_register_password").focus();
        return false;
    }

    if(userRegisterPwd !== againUserRegisterPwd){
        $("#again_user_register_passwords").children(".info_span").show().text("两次输入的密码不一致，请重新输入");
        //$("#again_user_register_password").focus();
        return false;
    }else{
        $("#again_user_register_passwords").children(".info_span").hide().text("");
    }

    var tel = $.trim($("#user_register_tel").val());
    if(tel.length == 0){
        $("#user_register_tels").children(".info_span").show().text("手机号码不能为空");
        //$("#user_register_tel").focus();
        return false;
    }

    var telText = $("#user_register_tels").children(".info_span").text();
    if(telText.length != 0){
        $("#user_register_tels").children(".info_span").show().text(telText);
        //$("#user_register_tel").focus();
        return false;
    }

    var code = $.trim($("#user_register_code").val());
    if(code.length == 0){
        $("#user_register_codes").children(".info_span").show().text("请输入验证码");
        //$("#user_register_code").focus();
        return false;
    }

    var param = {
        "code":code,
        "tel":tel
    };

    ajaxDataController(globalApi("ssoApi.check.user.code"), JSON.stringify(param), null, function (data) {
        if (data.code == 0) {
            //用户手机号验证码正确,可以注册
            $("#user_register_codes").children(".info_span").text("");

            var emailText = $("#user_register_emails").children(".info_span").text();
            if(emailText.length != 0){
                $("#user_register_emails").children(".info_span").show().text(emailText);
                return false;
            }

            //验证邮箱格式是否正确
            var email = $("#user_register_email").val();
            if(email.length == 0){
                $("#user_register_emails").children(".info_span").show().text("请输入邮箱");
                return false;
            }else{
                $("#user_register_emails").children(".info_span").hide().text("");
            }

            var checked = $("#service_agreement").is(":checked");
            if(!checked){
                $("#service_agreements").children(".info_span").show().text("请勾选同意协议");
                //$("#service_agreement").focus();
                return false;
            }else{
                $("#service_agreements").children(".info_span").hide().text("");
            }

            var param = {
                "userName":username,
                "tel":tel,
                "password":CryptoJS.MD5(userRegisterPwd).toString(),
                "email":email
            };

            registerUser(param,username,tel,email);

        } else {
            $("#user_register_codes").children(".info_span").show().text(data.msg);
            return false;
        }
    }, true, "post");

}

function registerUser(param,username,tel,email) {
    //用户注册
    ajaxDataController(globalApi("ssoApi.user.register"), JSON.stringify(param), null, function (data) {
        if (data.code == 0) {
            $("#divUserRegister").addClass("sso-div-hidden");
            $("#divRegisterForm").removeClass("sso-div-hidden");
            $("#register_name").text(username);
            $("#register_tel").text(tel);
            $("#register_email").text(email);
            userId = data.data.userId;
            setInterval("redirectToSuccess()", 1000);
        } else {
            layer.msg(data.msg);
            return false;
        }
    }, true, "post");
}

/*function userRegister() {
    $("#divUserRegister").addClass("sso-div-hidden");
    $("#divRegisterForm").removeClass("sso-div-hidden");
    $("#register_name").text("张三");
    $("#register_tel").text("18636967836");
    $("#register_email").text("qwt311@126.com");

    setInterval("redirectToSuccess('ed9f92a54fde438bb607a24684a6fc75')", 1000);

}*/
var numNew = 1;
var userId;
function redirectToSuccess(){

    document.getElementById("num").innerHTML=numNew;
    numNew--;
    if(numNew<0){
        document.getElementById("num").innerHTML=0;
        var redirectUrl = $.trim($("#redirectUrl").val());
        if(redirectUrl.length != 0){
            window.location.href=redirectUrl;
        }else{
            window.location.href="/app/loginSuccess?userId="+userId;
        }
    }
}

//服务协议的验证
function checkService() {
    var checked = $("#service_agreement").is(":checked");
    if(!checked){
        $("#service_agreements").children(".info_span").show().text("请勾选同意协议");
        //$("#service_agreement").focus();
        return false;
    }else{
        $("#service_agreements").children(".info_span").hide().text("");
    }
}
