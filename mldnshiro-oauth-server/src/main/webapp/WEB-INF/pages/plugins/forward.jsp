<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="/WEB-INF/pages/plugins/basepath.jsp"/>
<title>操作完成</title>
<script type="text/javascript" src="js/mldn.util.js"></script>
<script type="text/javascript">
	jsForwardUrl = "${basePath}${url}" ;
</script>
<script type="text/javascript" src="js/plugins/forward.js"></script>
</head>
<body>
<div>
	<h1>${msg}</h1>
	<h1><span id="jumpTime">3</span>秒后进行跳转，如果没有跳转请按<a href="${basePath}${url}">这里</a>！</h1>
</div>
</body>
</html>