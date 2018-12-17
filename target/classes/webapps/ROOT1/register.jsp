<%@ page language="java" import="java.util.*,com.zgx.test.*"
	contentType="text/html;charset=utf-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
<head>
<base href="<%=basePath%>">

<title>注册页面</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta charset="UTF-8">
<!--  
    <link rel="stylesheet" type="text/css" href="styles.css">  
    -->
<script language="javascript">
	function isValid(form) {
		if (form.username.value == "") {
			alert("用户名不能为空");
			return false;
		}
		if (form.pwd.value != form.pwd2.value) {
			alert("两次输入的密码不同！");
			return false;
		} else if (form.pwd.value == "") {
			alert("用户密码不能为空！");
			return false;
		} else
			return true;
	}
</script>
</head>

<body>
	<p>
		<font size="12" face="Times"> This is JSP . </font>
	</p>
	<%
		response.setCharacterEncoding("UTF-8");
		//		out.println("This is A Code1111111111111111---ROOT");
		//		System.out.println(session.getId());
		//		System.out.println(session.getId());
		//		System.out.println(a);
		request.getSession();
		request.getRequestDispatcher("test_font.html").include(request, response);
		if (a == 0) {
			session.setAttribute("1", "2");
			session.setAttribute("2", "2");
			session.setAttribute("3", "2");
			session.setAttribute("4", "2");
			session.setAttribute("5", "2");
			a++;
		}
		System.out.println(session.getLastAccessedTime());
		System.out.println(session.getAttribute("1"));
	%>
	<%!int a = 0;%>
	<%=a%>
	<%=a%>
	<% System.out.println("page run"); %>
	<p>
		<font size="10" face="Times"> This is another paragraph张. </font>
	</p>
	<%-- 注释 --%>
	<center>
		<body bgcolor="#e3e3e3">
			<h2>用户注册</h2>
			<form action="check2.jsp" method="post"
				onSubmit="return isValid(this);">
				<table>
					<tr>
						<td>用户名:</td>
						<td><input type="text" name="username" size="20" /></td>
					</tr>
					<tr>
						<td>输入密码dsfsdfa:</td>
						<td><input type="text" name="pwd" size="20" /></td>
					</tr>
					<tr>
						<td>再次确认密码:</td>
						<td><input type="text" name="pwd2" size="20" /></td>
					<tr>
					<tr>
						<td><input type="submit" value="注册" />
						<td><input type="reset" value="重置" />
				</table>
			</form>
	</center>
	<br>
</body>
</html>
