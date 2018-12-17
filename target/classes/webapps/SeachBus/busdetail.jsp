<%@page import="com.cwy.bean.Intro"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>

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

table {
	width: 100%;
	border-collapse: collapse;
	margin: 2m auto;
}

th, td {
	text-align: left;
	padding: 8px;
}

tr:nth-child(even) {
	background-color: #cfc;
}
</style>
<title></title>
</head>

<body>
	<header>
		公交线路查询后台管理<span>线路详细信息</span>
	</header>
	<div class="nav">
		<ul>
			<li><a href="<%=request.getContextPath()%>/ShowBusServlet">线路信息</a></li>
			<li><a href="">站点信息</a></li>
			<li><a href="#">管理员信息</a></li>
			<li><a href="#">用户信息</a></li>
		</ul>
	</div>
	<div class="content">
		<table>
			<tr>
				<th>线路编号</th>
				<th>线路名称</th>
				<th>详细信息</th>
			</tr>
			<%
				Intro intro = (Intro) request.getAttribute("intro");
			%>

			<tr>
				<td><%=intro.getIntro_bus_id()%></td>
				<td><%=intro.getBus_name()%></td>
				<td><%=intro.getIntro_text()%></td>
			</tr>





		</table>

	</div>
	<footer>create by cwy</footer>
</body>
</html>