
/**
 * Created by tianyuliang on 2016/10/22.
 */

/**
 * 获取URL参数
 */
function getUrlParameter(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var values = window.location.search.substr(1).match(reg);
    return values != null ? unescape(values[2]) : null;
}

/**
 * 获取根路径
 * （仅仅用于 java web 工程下的某些特定情况，并不具有大范围的普遍适用性。
 *  如果这个工具集插件被用于新的项目，又不知道这个方法用来做什么，敬请干掉这个方法，不要使用）
 * @param {String} url 用于和根路径拼接成完整的路径，这是一个可选参数 (url 前不用加 "/")
 * @return {String} 返回当前 web 应用根路径，如果有传入 url 参数，则返回一个拼接完整的路径
 */
function getWebRootPath(url) {
    var webroot = document.location.href;
    webroot = webroot.substring(webroot.indexOf('//') + 2, webroot.length);
    webroot = webroot.substring(webroot.indexOf('/') + 1, webroot.length);
    webroot = webroot.substring(0, webroot.indexOf('/'));
    if (url && url.indexOf(webroot) > 0) {
        return url;
    }
    var rootpath = "/" + webroot;
    return url ? (rootpath + '/' + url) : rootpath;
}

function myConsoleLog(strData) {
    try{
        console.log(strData);
    }catch (e){}
}

function find_element($element, el_name) {
    return $element.children("button[name='" + el_name + "']")[0];
}

/**
 * LayUI组件，常用方法
 * http://layer.layui.com/
 *
 * @param gotoUrl
 */

function gotoHref(gotoUrl) {
    window.location.href = gotoUrl;
}


function errorMsg(msg) {
    layer.msg(msg, {icon: 2, time: 1200});
}

function errorGotoUrl(msg, gotoUrl) {
    layer.msg(msg, {icon: 2, time: 50}, function () {
        window.location.href = gotoUrl;
    });
}

function successMsgFn(msg) {
    layer.msg(msg, {icon: 6, time: 600});
}

function successAndFn(msg, fn) {
    layer.msg(msg, {icon: 6, time: 300}, fn);
}

function successGotoUrl(msg, gotoUrl) {
    layer.msg(msg, {icon: 6, time: 300}, function () {
        window.location.href = gotoUrl;
    });
}

function tipsGotoUrl(title, content, gotoUrl) {
    layer.open({
        title: title,
        content: content,
        icon: 6,
        btnAlign: 'c',
        closeBtn: 0,
        time: 0,
        btn1: function () {
            window.location.href = gotoUrl;
        }
    });
}

function tipsContent(title, content) {
    // btn: [] 表示取消按钮，  closeBtn: 0 不显示关闭图标，  time: 0 默认不关闭浮层
    layer.alert(content, {icon: 6, time: 0, title: title, closeBtn: 0, btn: []});
}

function tipsLoginMsg(msg, domId) {
    layer.tips(msg, domId, {tips: [2, "#FF5722"], time: 1200});
}

function tipsInputParamMsg(msg, domId) {
    layer.tips(msg, domId, {tips: [3, "#FF5722"], time: 1200});
}

function tipsUserIdMsg(msg, domId) {
    layer.tips(msg, domId, {tips: [3, "#FF5722"], time: 1200});
}

/**
 * 修改数据，待确认框的交互提示
 */
function promptMsg(title, fn) {
    layer.prompt({title: title}, function (new_value, index) {
        fn(new_value);
        layer.close(index);
    });
}

function alertMsg(msg, fn) {
    layer.alert(msg, {icon: 6, time: 0}, fn);
}

function confirmMsg(msg, sureFn, cancelFn) {
    layer.confirm(msg, {btn: ['确定', '取消']}, sureFn, cancelFn);
}
