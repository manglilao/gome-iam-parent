/**
 * Created by tianyuliang on 2016/11/7.
 */

var user_id_selections = [];
contextPath="${rc.contextPath}";

$(function () {

    var user_sort_order = "";

    $("#table_user_list").bootstrapTable({
        url: globalApi("api.user.list"),
        dataField: "data",
        cache: false,
        striped: true,
        pagination: true,
        pageSize: 10,
        pageNumber: 1,
        pageList: [10, 20, 50],
        search: false,
        showRefresh: false,
        clickToSelect: false,
        toolbar: "#toolbar_user_list",
        sidePagination: "server",
        queryParamsType: "limit",
        sortName: "status",
        sortable: true,
        responseHandler: function (res) {
            // 2016/11/25 兼容后端API报文的节点与bootstrap-table默认报文节点不一致的问题 Add by tianyuliang
            return res.data;
        },
        queryParams: function getParams(params) {
            params.userName = $.trim($("#search_user_name").val());
            if ($("#user-status-sort").hasClass("glyphicon-arrow-down")) {
                user_sort_order = "desc";
            } else {
                user_sort_order = "asc";
            }
            params.order = user_sort_order; // 处理status字段排序，需要额外传递排序参数
            return params;
        },
        onLoadSuccess: function (data) {
            $('#batch_able').attr("disabled", !$("#table_user_list").bootstrapTable('getSelections').length);
            $('#batch_disable').attr("disabled", !$("#table_user_list").bootstrapTable('getSelections').length);
            return false;
        },
        columns: [
            {
                title: "全选",
                field: "select",
                checkbox: true,
                width: 20,
                align: "center",
                valign: "middle"
            },
            {
                field: "id",
                title: "ID",
                align: "center",
                valign: "middle"
            }, {
                field: "userName",
                title: "用户名",
                align: "center",
                valign: "middle",
                formatter: "user_name_formatter"
            }, {
                field: "realName",
                title: "真实姓名",
                align: "center",
                valign: "middle"
            },

            /* {
             field: "type",
             title: "员工类型",
             align: "center",
             valign: "middle",
             formatter: "user_type_formatter"
             },*/

            {
                field: "post",
                title: "用户岗位",
                align: "center",
                valign: "middle",
                formatter: "user_post_formatter"
            }, {
                field: "company",
                title: "所属公司",
                align: "center",
                valign: "middle",
                formatter: "user_company_formatter"
            }, {
                field: "tel",
                title: "手机号 ",
                align: "center",
                valign: "middle"
            }, {
                field: "createDate",
                title: "创建时间",
                align: "center",
                valign: "middle"
            }, {
                field: "status",
                title: "用户状态&nbsp;<div id='user-status-sort' class='glyphicon glyphicon-arrow-down div-user-order-cursor'></div>",
                align: "center",
                valign: "middle",
                formatter: "user_status_formatter"
            }, {
                field: "status",
                title: "操作",
                align: "center",
                valign: "middle",
                formatter: "user_handle_formatter"
            }
        ],
        formatNoMatches: function () {
            return "无符合条件的记录";
        }
    });


    // 重绘、渲染页面
    $(window).resize(function () {
        $("#table_user_list").bootstrapTable("resetView");
    });

    $("#search_user_name").keydown(function (event) {
        if (event.keyCode == 13) {
            search_user_name();
            event.preventDefault();
            return true;
        }
    });

    // 单击选中/取消选中/单击全选/取消全选 四个事件
    $("#table_user_list").on('check.bs.table uncheck.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        if ($("#table_user_list").bootstrapTable('getSelections').length > 0) {
            $("#batch_disable").removeAttr("disabled");
            $("#batch_able").removeAttr("disabled");
        } else {
            $("#batch_disable").attr("disabled", "disabled");
            $("#batch_able").attr("disabled", "disabled");
        }
        user_id_selections = build_selections_id();
    });

    $("#user-status-sort").click(function (event) {
        user_sort_order = handler_user_status_css(event.target);
        myConsoleLog("user_sort_order=" + user_sort_order);
        search_user_name();
        event.preventDefault();
        return true;
    });

    // 启用bootstrap的气泡提示
    $("[rel='tooltip']").tooltip();

})


function search_user_name(def_option) {
    var refresh_option = def_option || {};
    if (!refresh_option.hasOwnProperty("url")) {
        //TODO 2016/11/30 巨坑！  修复refresh事件绑定table后，pageNumber属性没有重置的问题 Add by tianyuliang
        refresh_option.url = globalApi("api.user.list");
    }
    $("#table_user_list").bootstrapTable("refresh", refresh_option);
}

function handler_user_status_css(target) {
    var sort_order = "";
    if ($(target).hasClass("glyphicon-arrow-down")) {
        sort_order = "desc";
        $(target).removeClass("glyphicon glyphicon-arrow-down");
        $(target).addClass("glyphicon glyphicon-arrow-up");
    } else {
        sort_order = "asc";
        $(target).removeClass("glyphicon glyphicon-arrow-up");
        $(target).addClass("glyphicon glyphicon-arrow-down");
    }
    return sort_order;
}

function user_type_formatter(value, row, index) {
    return row.type === 1 ? "第三方员工" : "正式员工";
}

function user_post_formatter(value, row, index) {
    if(row.post == null){
        var postHtml = '<div rel="tooltip" title="' + row.post +'" > ' + '……' + '</div>';
    }else{
        var content = row.post.length > 6 ? (row.post.toString().substr(0, 6) + "……") : row.post;
        var postHtml = '<div rel="tooltip" title="' + row.post +'" > ' + content + '</div>';
    }
    return postHtml;
}

function user_company_formatter(value, row, index) {
    if(row.company == null){
        var companyHtml = '<div rel="tooltip" title="' + row.company +'" > ' + "……" + '</div>';
    }else{
        var content = row.company.length > 6 ? (row.company.toString().substr(0, 6) + "……") : row.company;
        var companyHtml = '<div rel="tooltip" title="' + row.company +'" > ' + content + '</div>';
    }
    return companyHtml;
}

function user_name_formatter(value, row, index) {
    var content = row.userName.length > 20 ? (row.userName.toString().substr(0, 20) + "……") : row.userName;
    var userNameHtml = '<a href="/web/userUpdate?id=' + row.id + '" title="' + row.userName + '" rel="tooltip" >' + content + '</a>';
    return userNameHtml;
}

function user_status_formatter(value, row, index) {
    //  接口返回row.status  1:状态正常，  0:已禁用
    if (row.status === 1) {
        return '<span class="success">正常</span>';
    }
    return '<span class="alert-danger">已禁用</span>';
}

function user_handle_formatter(value, row, index) {
    //  接口返回row.status  1:状态正常，  0:已禁用
    //  注意：此处的按钮文字要标识相反的描述，便于将更新后的值传回给接口
    var deleteHtml = ''
        + '<button type="button" class="btn btn-default btn-sm btn-info" name="delete-user" onclick="trigger_user_list_event()" '
        + ' userId="' + row.id + '" userName="' + row.userName + '" realName="' + row.realName + '" >删除'
        + '</button>&nbsp;&nbsp;&nbsp;&nbsp;';

    var resetHtml = ''
        + '<button type="button" class="btn btn-default btn-sm btn-info" name="reset-pwd" onclick="trigger_user_list_event()" '
        + ' userId="' + row.id + '" userName="' + row.userName + '" realName="' + row.realName + '" >重置密码'
        + '</button>&nbsp;&nbsp;&nbsp;&nbsp;';

    var reviewHtml = '';
    if (row.status === 1) {
        reviewHtml = ''
            + '<button type="button" class="btn btn-default btn-sm btn-success" name="review-user" onclick="trigger_user_list_event()" '
            + ' userId="' + row.id + '" userName="' + row.userName + '" oldStatus="' + row.status + '" >禁用'
            + '</button>';
    } else {
        reviewHtml = ''
            + '<button type="button" class="btn btn-default btn-sm btn-info" name="review-user" onclick="trigger_user_list_event()" '
            + ' userId="' + row.id + '" userName="' + row.userName + '" oldStatus="' + row.status + '" >启用'
            + '</button>';
    }
    return deleteHtml + resetHtml + reviewHtml;
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
function trigger_user_list_event() {
    $("#table_user_list").unbind("click").bind("click", function (e) {
        handler_delete_user(e.target, "delete-user");               // 删除用户
        handler_reset_password(e.target, "reset-pwd");              // 重置密码
        handler_review_user_single(e.target, "review-user");        // 审核用户
        e.preventDefault();
        return true;
    });
}

function build_selections_id() {
    return $.map($("#table_user_list").bootstrapTable("getSelections"), function (row) {
        return row.id;
    });
}


