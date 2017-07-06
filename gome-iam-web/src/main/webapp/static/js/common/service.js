/**
 * Created by qiaowentao on 2017/7/4.
 */

$(function () {

    var redirectUrl = $.trim($("#redirectUrl").val());

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