<%@page import="com.cwy.bean.Page"%>
<%@page import="com.cwy.bean.Bus"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<% System.out.println(out); %>
<%--  --%>
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
		公交线路查询后台管理<span>线路信息列表</span>
	</header>
	<div class="nav">
		<ul>
			<li><a href="NewBus.jsp">新增线路</a></li>
			<li><a href="<%=request.getContextPath()%>/ShowSiteServlet">站点信息</a></li>
			<li><a href="#">管理员信息</a></li>
			<li><a href="#">用户信息</a></li>
		</ul>
	</div>
	<div class="content" style="height: 420px; position: relative;">
		<table>
			<tr>
				<th>编号</th>
				<th>线路名称</th>
				<th>类型</th>
				<th>站点数量</th>
				<th>详细信息</th>
				<th>删除</th>
			</tr>
			<%
				List<Bus> busList = (List<Bus>) request.getAttribute("busList");
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
				String value = (String) request.getAttribute("value");
				String type = (String) request.getAttribute("type");
				System.out.println(type);
				if (value == null)
					value = "";
				if (nextpage > lastpage) {
					nextpage--;
				}
				if (previouspage < fristpage) {
					previouspage++;
				}
				if(busList!=null&&busList.size()>0){
				for (int i = 0; i < busList.size(); i++) {
					Bus bus = busList.get(i);
			%>

			<tr>
				<td><%=bus.getBus_id()%></td>
				<td><%=bus.getBus_name()%></td>
				<td>
					<%
						if (bus.getBus_type() == 1) {
								out.print("上行");
							} else {
								out.print("下行");
							}
					%>
				</td>
				<td><%=bus.getBus_site_number()%></td>
				<td>
					<form action="<%=request.getContextPath()%>/BusModifyServlet"
						method="get">
						<input type="hidden" name="id" value="<%=bus.getBus_id()%>"> <input
							type="hidden" name="btn" value="detail"> <input
							type="submit" value="详情">
					</form>
				</td>
				<td>
					<form action="<%=request.getContextPath()%>/BusModifyServlet"
						method="post">
						<input type="hidden" name="id" value="<%=bus.getBus_id()%>"> <input
							type="hidden" name="btn" value="del"> <input
							type="submit" value="删除">
					</form>
				</td>
			</tr>
			<%
				}
			}else{%>
			<tr>
			<td>还没有数据</td>
			</tr>		
				<% }
			%>
		</table>

		<div style="position: absolute; bottom: 0; left: 0;">
			<form action="<%=request.getContextPath()%>/ShowBusServlet"
				method="post">
				按<select  name="type" onchange="checked(<%=type%>);">
					<option value="busname">名称</option>
					<option value="busid">编号</option>
				</select> 搜索： <input type="text" name="value" value="<%=value%>"> <input
					type="submit" name="seach" value="搜索" onsubmit="checked(<%=type%>);">
			</form>
		</div>
		<div style="position: absolute; bottom: 0; right: 0;">
			<a
				href="<%=request.getContextPath()%>/ShowBusServlet?currentPage=<%=fristpage%>&value=<%=value%>&type=<%=type%>">首页</a>
			<a
				href="<%=request.getContextPath()%>/ShowBusServlet?currentPage=<%=previouspage%>&value=<%=value%>&type=<%=type%>"
				onclick="openwin2(<%=currentpage%>,<%=fristpage%>);">前一页</a> <a
				href="<%=request.getContextPath()%>/ShowBusServlet?currentPage=<%=nextpage%>&value=<%=value%>&type=<%=type%>"
				onclick="openwin(<%=currentpage%>,<%=lastpage%>);">后一页</a> <a
				href="<%=request.getContextPath()%>/ShowBusServlet?currentPage=<%=lastpage%>&value=<%=value%>&type=<%=type%>">尾页</a>
		</div>

	</div>
	<footer>create by cwy</footer>
</body>
</html>