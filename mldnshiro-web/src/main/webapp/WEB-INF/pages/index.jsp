<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<jsp:include page="/WEB-INF/pages/plugins/basepath.jsp"/>
</head>
<body>
<h1>登录成功，【<a href="logout.shiro">系统注销</a>】</h1> 
<h2>登录ID：<shiro:principal/>、真实姓名：${name}</h2>
<shiro:hasRole name="dept">  
	<h2>用户具有部门管理角色！</h2>
	<shiro:hasPermission name="dept:list">
		<h3><a href="pages/admin/dept/list.action">查看部门列表</a>！</h3>
		<h3><a href="pages/admin/dept/get.action?did=10">查看部门信息</a>！</h3>
	</shiro:hasPermission>
</shiro:hasRole>
</body>
</html>