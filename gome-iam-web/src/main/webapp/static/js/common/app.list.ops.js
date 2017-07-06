$(function () {
    $('#table_app_list').bootstrapTable({
        url: globalApi("api.app.list"),
        dataField: "data",
        cache: false,
        striped: true,
        pagination: true,
        pageSize: 10,
        pageNumber: 1,
        pageList: [10, 20, 50],
        search: false,
        showRefresh: false,
        clickToSelect: true,
        toolbar: "#toolbar_app_list",
        sidePagination: "server",
        queryParamsType: "limit",
        responseHandler: function (res) {
            return res.data;
        },
        queryParams: function getParams(params) {
            params.appName = $.trim($("#search_app_name").val());
            return params;
        },
        columns: [ {
            field: "appId",
            title: "AppID",
            align: "center",
            valign: "middle"
        }, {
            field: "appName",
            title: "应用名称",
            align: "center",
            valign: "middle",
            formatter: "app_name_formatter"
        }, {
            field: "appKey",
            title: "AppKey",
            align: "center",
            valign: "middle"
        }, {
            field: "domain",
            title: "应用域名",
            align: "center",
            valign: "middle"
        }, {
            field: "createDate",
            title: "创建时间",
            align: "center",
            valign: "middle"
        }, {
            field: "status",
            title: "应用状态",
            align: "center",
            valign: "middle",
            formatter: "app_status_formatter"
        }, {
            field: "status",
            title: "操作",
            align: "center",
            valign: "middle",
            formatter: "app_handle_formatter"
        }],
        formatNoMatches: function () {
            return "无符合条件的记录";
        }
    });


    // 重绘、渲染页面
    $(window).resize(function () {
        $('#table_app_list').bootstrapTable('resetView');
    });

    $("#search_app_name").keydown(function(event){
        if(event.keyCode == 13){
            search_app_name();
            event.preventDefault();
            return true;
        }
    });

})


function search_app_name(def_option) {
    var refresh_option = def_option || {};
    if (!refresh_option.hasOwnProperty("url")) {
        //TODO 2016/11/30 巨坑！ 修复refresh事件绑定table后，pageNumber属性没有重置的问题 Add by tianyuliang
        refresh_option.url = globalApi("api.app.list");
    }
    $("#table_app_list").bootstrapTable("refresh", refresh_option);
}

function app_name_formatter(value, row, index) {
    return '<a href="/web/appUpdate?appId=' + row.appId + '"'
        + ' appId="' + row.appId  + '" appName="' + row.appName + '" domain="' + row.domain + '" appKey="' + row.appKey + '"'
        + ' ip="' + row.ip + '" appUrl="' + row.appUrl + '" expiredApi="' + row.expiredApi + '" status="' + row.status + '" >'
        + row.appName + '</a>';
}

function app_status_formatter(value, row, index) {
    //  接口返回row.status  1:状态正常，  0:已禁用
    if(row.status === 1){
        return '<span class="success">正常</span>';
    }
    return '<span class="alert-danger">已禁用</span>';
}

function app_handle_formatter(value, row, index) {
    //  接口返回row.status  1:状态正常，  0:已禁用
    //  注意：此处的按钮文字要标识相反的描述，便于将更新后的值传回给接口
    var btnHtml = '';
    if (row.status === 1) {
        btnHtml = ''
            + '<button type="button" class="btn btn-default btn-sm btn-info" name="review-app" onclick="trigger_app_list_event()" '
            + ' appId="' + row.appId + '" appName="' + row.appName + '" domain="' + row.domain + '" appKey="' + row.appKey + '"'
            + ' ip="' + row.ip + '" appUrl="' + row.appUrl + '" expiredApi="' + row.expiredApi + '" oldStatus="' + row.status + '">禁用' +
            '</button>';
    } else {
        btnHtml = ''
            + '<button type="button" class="btn btn-default btn-sm btn-success" name="review-app" onclick="trigger_app_list_event()" '
            + ' appId="' + row.appId + '" appName="' + row.appName + '" domain="' + row.domain + '" appKey="' + row.appKey + '"'
            + ' ip="' + row.ip + '" appUrl="' + row.appUrl + '" expiredApi="' + row.expiredApi + '" oldStatus="' + row.status + '">启用' +
            '</button>';
    }
    return btnHtml;
}

/**
 * 注意：
 * （1）bind()与unbind()要配套使用，防止同一个dom元素的click事件累加，天坑啊！！！！
 * （2）$(target).click(function(){})  与 $(target).unbind("click").bind("click", function(){}) 效果相同，但写法不一致
 *
 * @author tianyuliang
 * @since 2016/11/25
 * @params
 */
function trigger_app_list_event() {
    $("#table_app_list").unbind("click").bind("click", function (e) {
        handler_review_app(e.target, "review-app");       // 启用禁用app
        e.preventDefault();
        return true;
    });
}


