/**
 * Created by qiaowentao on 2017/6/22.
 */

$(function () {

    getUserInfo();
    $("[data-toggle='popover']").popover();
});

function getUserInfo() {

    var uid = $.cookie("userId");
    if(uid.length == 0){
        return false;
    }
    var param = {
        "uuid":uid
    };

    ajaxDataController(globalApi("ssoApi.user.getUserInfo"), JSON.stringify(param), null, function (data) {
        if (data.code == 0) {
           /* $("#user_post").attr('data-content', data.data.post || "");
            $("#last_login_time").attr('data-content', data.data.lastLoginDate);*/
            $("#user_name").html(data.data.userName+"已登录");
        }
    }, true, "post");

}