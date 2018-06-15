<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<jsp:include page="/WEB-INF/pages/plugins/basepath.jsp"/>
</head>
<body>
<%
	String login_url = "loginShiro.action" ;
%>
<h1>
<c:if test="${msg == 'org.apache.shiro.authc.IncorrectCredentialsException'}">
	登录密码错误！
</c:if>
<c:if test="${msg == 'org.apache.shiro.authc.UnknownAccountException'}">
	该用户名不存在！
</c:if>
<c:if test="${msg == 'org.apache.shiro.authc.LockedAccountException'}">
	该用户已经被锁定！
</c:if> 
</h1>
${error}
<form action="<%=login_url%>" method="post">
	用户名：<input type="text" name="mid" value="admin"><br>
	密码：<input type="text" name="password" value="hello"><br>
	验证码：<input type="text" name="code" id="code"><img src="ImageCode"><br>
	<input type="checkbox" name="rememberme" value="true"/>下次免登录<br>
	<input type="submit" value="登录">
</form>
</body>
</html>