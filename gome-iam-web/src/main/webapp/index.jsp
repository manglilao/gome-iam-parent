<%--<%
    response.sendRedirect(request.getContextPath() + "/app/toLogin");
%>--%>
<script type="text/javascript" src="${rc.contextPath}/static/js/common/web.data.common.js"></script>
<script type="text/javascript" src="${rc.contextPath}/static/js/common/util.js"></script>
<script type="text/javascript" src="${rc.contextPath}/static/js/jquery/jquery-1.12.4.js"></script>
<script type="text/javascript">
    $(function () {
        var contextPath="${rc.contextPath}";
        var redirectUrl = getUrlParameter("redirectUrl");
        if(redirectUrl === null){
            window.location.href = contextPath+"/index/toLogin";
        }else{
            window.location.href = contextPath+"/index/toLogin?redirectUrl="+redirectUrl;
        }

    });
</script>