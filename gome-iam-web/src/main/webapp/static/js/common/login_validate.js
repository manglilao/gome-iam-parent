/**
 * Created by Administrator on 2016/8/9.
 */


$(function () {
    $('#bxslider').bxSlider({
        auto:true,  //自动轮播
    });

    $("[data-toggle='popover']").popover();

    $("#copyrightYear").html(new Date().getFullYear());
    /**
     * 此处为什么和login.html里的一样请求两遍呢？我（by liuhaikun-ds）先注释了
     */
     //query_sso_userinfo();
     //load_sso_app_list();
     $("#txt_password").keypress(function (event) {
         if (event.keyCode == "13") {
             //sso_login();
         }
     });

     $("#btn_login").click(function () {
         //sso_login();
     });

});