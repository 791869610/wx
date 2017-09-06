<%--
  Created by IntelliJ IDEA.
  User: wenda
  Date: 6/15/2017
  Time: 2:06 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>登陆贾浩世界</h1>
<hr>
<form action="/wxCallBack" method="post">
    账号：<input type="text" name="account"><br>
    密码：<input type="password" name="password"><br>
    <input type="text" name="openid" value="${openid}">
    <input type="submit" value="登录并绑定">
</form>
</body>
</html>