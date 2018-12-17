package com.zgx.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.io.*;

public class register_jsp extends HttpServlet {
	int a = 0;

	public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		ServletContext application = this.getServletContext();
		HttpSession session = req.getSession();
		HttpServletRequest request = req;
		HttpServletResponse response = resp;
		HttpServlet page = this;
		ServletConfig config = this.getServletConfig();
		out.write("");

		out.write("  ");
		request.getRequestDispatcher("test_font.html").forward(request, response);
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path
				+ "/";
		System.out.println(session.getId());
		System.out.println(a);
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
		out.write("  \r\n");

		out.write("  \r\n");

		out.write("<html>  \r\n");

		out.write("  <head>  \r\n");

		out.write("    <base href=\"");

		out.write(basePath);
		out.write("\">  \r\n");

		out.write("      \r\n");

		out.write("    <title>注册页面</title>  \r\n");

		out.write("      \r\n");

		out.write("    <meta http-equiv=\"pragma\" content=\"no-cache\">  \r\n");

		out.write("    <meta http-equiv=\"cache-control\" content=\"no-cache\">  \r\n");

		out.write("    <meta http-equiv=\"expires\" content=\"0\">      \r\n");

		out.write("    <meta http-equiv=\"keywords\" content=\"keyword1,keyword2,keyword3\">  \r\n");

		out.write("    <meta http-equiv=\"description\" content=\"This is my page\">  \r\n");

		out.write("    <!--  \r\n");

		out.write("    <link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\">  \r\n");

		out.write("    -->  \r\n");

		out.write("<script language=\"javascript\">    \r\n");

		out.write("function isValid(form)    \r\n");

		out.write("{    \r\n");

		out.write("if (form.username.value==\"\")    \r\n");

		out.write(" {    \r\n");

		out.write(" alert(\"用户名不能为空\");    \r\n");

		out.write(" return false;    \r\n");

		out.write(" }    \r\n");

		out.write("if (form.pwd.value!=form.pwd2.value)    \r\n");

		out.write("{    \r\n");

		out.write("alert(\"两次输入的密码不同！\");    \r\n");

		out.write("return false;    \r\n");

		out.write("}    \r\n");

		out.write("else  if (form.pwd.value==\"\")    \r\n");

		out.write("{    \r\n");

		out.write("alert(\"用户密码不能为空！\");    \r\n");

		out.write("return false;    \r\n");

		out.write("}    \r\n");

		out.write("else return true;    \r\n");

		out.write("}    \r\n");

		out.write("</script>    \r\n");

		out.write("</head>  \r\n");

		out.write("   \r\n");

		out.write("  <body>  \r\n");

		out.write("  ");

		out.println("This is A Code");
		out.write("\r\n");

		out.write("  ");

		out.write("\r\n");

		out.write("  ");

		out.write(a);
		out.write("\r\n");

		out.write("  ");

		// 注释
		out.write("\r\n");

		out.write("  <center>  \r\n");

		out.write("   <body bgcolor=\"#e3e3e3\">  \r\n");

		out.write("  <h2>用户注册</h2>  \r\n");

		out.write("  <form action=\"check2.jsp\" method=\"post\" onSubmit=\"return isValid(this);\">  \r\n");

		out.write("<table> \r\n");

		out.write("  <tr><td>用户名:</td><td><input type=\"text\" name=\"username\" size=\"20\"/></td></tr>  \r\n");

		out.write("  <tr><td>输入密码:</td><td><input type=\"text\" name=\"pwd\" size=\"20\"/></td></tr>  \r\n");

		out.write("  <tr><td>再次确认密码:</td><td><input type=\"text\"name=\"pwd2\" size=\"20\"/></td><tr>  \r\n");

		out.write("  <tr><td><input type=\"submit\" value=\"注册\"/><td><input type=\"reset\" value=\"重置\"/>  \r\n");

		out.write("  </table>  \r\n");

		out.write("</form>  \r\n");

		out.write("  </center>  \r\n");

		out.write("   <br>  \r\n");

		out.write("  </body>  \r\n");

		out.write("</html>  ");

	}
}