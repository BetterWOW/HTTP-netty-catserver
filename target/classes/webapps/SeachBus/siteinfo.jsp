<%@page import="com.cwy.bean.Site"%>
<%@page import="com.cwy.bean.Page"%>
<%@page import="com.cwy.bean.Bus"%>
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
<script type="text/javascript">
   function openwin(current,last) {
	if(current==last){
		alert("已经是最后一页");
	   }
   }
</script>
<script type="text/javascript">
  function openwin2(current,frist) {
	if(current==frist){
	alert("已经是首页");
	   }
	}
 </script>
<script type="text/javascript">
 function checked(checkValue){  
	    var select = document.getElementByName("type");  

	    for (var i = 0; i < select.options.length; i++){  
	        if (select.options[i].value == checkValue){  
	            select.options[i].selected = true;  
	            break;  
	        }  
	    }  
	}
 
</script>






<title></title>
</head>

<body>
	<header>
		公交线路查询后台管理<span>站点信息列表</span>
	</header>
	<div class="nav">
		<ul>
			<li><a href="<%=request.getContextPath()%>/ShowBusServlet">线路信息</a></li>
			<li><a href="NewSite.jsp">新增站点</a></li>
			<li><a href="#">管理员信息</a></li>
			<li><a href="#">用户信息</a></li>
		</ul>
	</div>
	<div class="content" style="height: 420px; position: relative;">
		<table>
			<tr>
				<th>站点编号</th>
				<th>线路编号</th>
				<th>线路名称</th>
				<th>站点名称</th>
				<th>站点顺序</th>
				<th>删除</th>
			</tr>
			<%
				List<Site> siteList = (List<Site>) request.getAttribute("siteList");
				Page p = (Page) request.getAttribute("page");
				int currentpage = p.getCurrentpage();
				int nextpage = currentpage + 1;
				int previouspage = currentpage - 1;
				int fristpage = 1;
				int lastpage =0;
				if(p.getTatalcount()%10==0){
					lastpage = p.getTatalcount() / 10;
				}else{
					lastpage = p.getTatalcount() / 10+1;
				}
				request.setCharacterEncoding("UTF-8");
				String value = (String) request.getAttribute("value");
				String type = (String) request.getAttribute("type");
				if (value == null)
					value = "";
				if (nextpage > lastpage) {
					nextpage--;
				}
				if (previouspage < fristpage) {
					previouspage++;
				}
				if (siteList != null && siteList.size() > 0) {
					for (int i = 0; i < siteList.size(); i++) {
						Site site = siteList.get(i);
						int busid = site.getSite_bus_id();
						String busname = null;
						if (busid % 2 == 0) {
							busname = site.getBusname() + "(下行)";
						} else {
							busname = site.getBusname() + "(上行)";
						}
			%>

			<tr>
				<td><%=site.getSite_id()%></td>
				<td><%=site.getSite_bus_id()%></td>
				<td><%=busname%></td>
				<td><%=site.getSite_name()%></td>
				<td><%=site.getSite_order()%></td>
				<td>
					<form action="<%=request.getContextPath()%>/DelSiteServlet"
						method="post">
						<input type="hidden" name="id" value="<%=site.getSite_id()%>"> <input
							type="hidden" name="btn" value="del"> <input
							type="submit" value="删除">
					</form>
				</td>
			</tr>
			<%
				}
				} else {
			%>
			<tr>
				<td>还没有数据</td>
			</tr>
			<%
				}
			%>
		</table>

		<div style="position: absolute; bottom: 0; left: 0;">
			<form action="<%=request.getContextPath()%>/ShowSiteServlet"
				method="post">
				按<select name="type" onchange="checked(<%=type%>);">
					<option value="sitename">站点名称</option>
					<option value="busid">线路编号</option>
				</select> 搜索： <input type="text" name="value" value="<%=value%>"> <input
					type="submit" name="seach" value="搜索"
					onsubmit="checked(<%=type%>);">
			</form>
		</div>
		<div style="position: absolute; bottom: 0; right: 0;">
			<a
				href="<%=request.getContextPath()%>/ShowSiteServlet?currentPage=<%=fristpage%>&value=<%=value%>&type=<%=type%>">首页</a>
			<a
				href="<%=request.getContextPath()%>/ShowSiteServlet?currentPage=<%=previouspage%>&value=<%=value%>&type=<%=type%>"
				onclick="openwin2(<%=currentpage%>,<%=fristpage%>);">前一页</a> <a
				href="<%=request.getContextPath()%>/ShowSiteServlet?currentPage=<%=nextpage%>&value=<%=value%>&type=<%=type%>"
				onclick="openwin(<%=currentpage%>,<%=lastpage%>);">后一页</a> <a
				href="<%=request.getContextPath()%>/ShowSiteServlet?currentPage=<%=lastpage%>&value=<%=value%>&type=<%=type%>">尾页</a>
		</div>

	</div>
	<footer>create by cwy</footer>
</body>
</html>