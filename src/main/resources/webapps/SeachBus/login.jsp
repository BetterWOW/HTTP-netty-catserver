<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <link rel="stylesheet" href="./css/main.css">
</head>
<body>
    <div class="loginGRS-banner">
        <div class="loginGRS-top">
        </div>
        <div class="loginGRS-main">
            <div class="all-center">
                <div class="loginGRS-login">
                    <div class="login-left">
                        <p style="font-size:30px;margin-top: 100px;">公交线路后台管理登陆</p>
                    </div>
                    <div class="login-right">
                        <form style="padding:20px 40px;" name="myForm" role="form" action="<%=request.getContextPath()%>/LoginServlet">
                            <!-- 用户名 -->
                            <div class="form-group">
                                <label for="_name">用户名</label>
                                <div class="form-div">
                                    <input id="_name" name="username" required="required" placeholder="请输入用户名">
                                </div>
                            </div>
                            <!--密码-->
                            <div class="form-group">
                                <label for="_password">密码</label>
                                <div class="form-div">
                                    <input id="_password" type="password" name="password" required="required" placeholder="请输入密码">
                                </div>
                            </div>
                            <%String msg=(String)request.getAttribute("msg");
                                		if(msg!=null&&!msg.equals("")){%>
                            <!-- 错误提示信息 -->
                            <div style="color:red;margin-left: 60px;margin-top: 10px;"><%=msg%></div><%} %>
                            <!--忘记密码-->

                            <!-- 确认登陆 -->
                            <div class="form-group">
                                <button type="submit">登陆</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>