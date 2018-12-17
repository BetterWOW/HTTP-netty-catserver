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
	width: 10em;
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
#busidtypewarning{
	color: red;
	display:none;
}
#ordertypewarning{
	color: red;
	display:none;
}

</style>

<title></title>
</head>

<body>
	<header>
		公交线路查询后台管理<span>新增站点信息</span>
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
	
		<form name="newSite" action="<%=request.getContextPath()%>/AddSiteServlet" method="post">
			<div>
				<label>站点所属线路编号：</label>
				<input id="busidname" name="busid" type="text" placeholder="站点所属线路编号" required="required" onkeyup="onBusidKeyUp();">
				<span id="busidtypewarning">线路编号必须为数字</span>
			</div>
			<div>
				<label>站点名称：</label>
				<input name="sitename" placeholder="站点名称" required="required">
			</div>
			<div>
				<label>站点所在顺序（即在原有顺序之后添加站点）：</label>
				<input id="ordername" name="order" placeholder="站点所在顺序" required="required" onkeyup="onOrderKeyUp();">
				<span id="ordertypewarning">站点顺序必须为数字</span>
			</div>

			<div>
				<input type="submit" value="添加">
			</div>
		</form>

	</div>
	<footer>create by cwy</footer>
	<script>

		function onBusidKeyUp(){
			$("busidtypewarning").style.display="none";
			var siteName=$("busidname").value;
			var reg = new RegExp('^[0-9]*$')
			if(!(reg.test(siteName))&&siteName!=''){
				$("busidtypewarning").style.display="inline-block";
			}else{
				$("busidtypewarning").style.display="none";
			}
			
		}
		function onOrderKeyUp(){
			$("ordertypewarning").style.display="none";
			var siteName=$("ordername").value;
			var reg = new RegExp('^[0-9]*$')
			if(!(reg.test(siteName))&&siteName!=''){
				$("ordertypewarning").style.display="inline-block";
			}else{
				$("ordertypewarning").style.display="none";
			}
			
		}
		
		//原生JS获取DOM封装
		function $(id) {
			return document.getElementById(id);
		}
	</script>
</body>
</html>