<%--
  Created by IntelliJ IDEA.
  User: wenda
  Date: 6/15/2017
  Time: 2:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <h1>贾浩世界账号注册</h1>
    <hr>
    <form action="/wxCallBack" method="post">
        账号：<input type="text" name="account"><br>
        密码：<input type="password" name="password"><br>
        昵称：<input type="text" name="nickname"><br>
        <input type="hidden" name="reg" value="1">
        <input type="hidden" name="openid" value="${openid}">
        <input type="submit" value="注册">
    </form>
</head>
<body>

</body>
</html>
