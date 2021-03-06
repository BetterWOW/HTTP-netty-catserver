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
	width: 6em;
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
		公交线路查询后台管理<span>新增线路信息</span>
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
	
		<form action="<%=request.getContextPath()%>/AddBusServlet" method="post">
			<div>
				<label>线路名称：</label>
				<input name="busname" placeholder="线路名称" required="required">
			</div>
			<div>
				<label>类型：</label>
				<select name="type">
					<option value="1">上行（1）</option>
					<option value="0">下行（0）</option>
				</select>
			</div>
			<div>
				<label>起始站点：</label>
				<input name="start" placeholder="起始站点" required="required">
			</div>
			<div>
				<label>终点站：</label>
				<input name="end" placeholder="终点站" required="required">
			</div>
			<div>
				<label>运行时间：</label>
				<input name="runtime" placeholder="运行时间" required="required">
			</div>
			<div>
				<label>车票价格：</label>
				<select name="price">
					<option value="1">1</option>
					<option value="2">2</option>
					<option value="3">3</option>
				</select>
				<label>元</label>
			</div>
			<div>
				<input type="submit" value="添加">
			</div>
		</form>

	</div>
	<footer>create by cwy</footer>
</body>
</html>