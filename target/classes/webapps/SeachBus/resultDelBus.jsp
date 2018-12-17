<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<style media="screen">
* {
	margin: 0;
	padding: 0;
}

header {
	padding: 1em 0;
	text-align: center;
	background-color: #69c;
	color: #fff;
	font-size: 300%;
}

header span {
	font-size: 50%;
	margin-left: 1em;
	color: #eee;
}

footer {
	position: absolute;
	bottom: 0;
	padding: 2em 0;
	text-align: center;
	background-color: #69c;
	color: #fff;
	width: 100%;
	clear: both;
}

.content {
	width: 75%;
	overflow: auto;
	float: right;
	min-height: 300px;
}

.nav {
	float: left;
	background-color: #eee;
	width: 25%;
	position: fixed;
	height: 100%;
	overflow: auto;
}

ul {
	list-style-type: none;
	margin-top: 1em;
}

li a {
	display: block;
	color: #000;
	padding: 8px 0 8px 16px;
	text-decoration: none;
}

li a:hover {
	background-color: #69c;
	color: white;
}
form {
	margin: 2em 4em;
}
form div {
	margin-bottom: 1em;
}
label {
	display: inline-block;
	width: 25em;
	text-align: right;
	margin-right: 2em;
}
input, select {
	font-size: 150%;
}
input[type=submit] {
	padding: 0 4em;
	margin-left: 6em;
}

</style>

<title></title>
</head>

<body>
	<header>
		公交线路查询后台管理<span>删除线路结果</span>
	</header>
	<div class="nav">
		<ul>
			<li><a href="<%=request.getContextPath()%>/ShowBusServlet">线路信息</a></li>
			<li><a href="<%=request.getContextPath()%>/ShowSiteServlet">站点信息</a></li>
			<li><a href="#">管理员信息</a></li>
			<li><a href="#">用户信息</a></li>
		</ul>
	</div>
	<div class="content">
	<%String msg=(String)request.getAttribute("msg");
	  String btn=(String)request.getAttribute("btn");
	 %>
	
		<form action="<%=request.getContextPath()%>/ShowBusServlet" method="post">
			<div>
				<label><%=msg %></label>
			</div>
			<div>
				<input type="submit" value="<%=btn%>">
			</div>
		</form>

	</div>
	<footer>create by cwy</footer>
</body>
</html>