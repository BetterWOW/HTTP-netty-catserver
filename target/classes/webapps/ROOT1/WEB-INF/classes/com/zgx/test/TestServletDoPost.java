package com.zgx.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.Session;

/**
 * Servlet implementation class TestServletDoPost
 */
@WebServlet("/TestServletDoPost")
public class TestServletDoPost extends HttpServlet {
	private int single = 0;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//resp.getWriter().println("ok");
		req.getCookies();
		req.getSession()
        resp.setContentType("text/html");    
        PrintWriter out = resp.getWriter();    
        out.println("<html>");    
        out.println("<head>");    
        out.println("<title>Hello World</title>");    
        out.println("</head>");    
        out.println("<body>");    
        out.println("<h1>Hello World!</h1>");    
        out.println("</body>");    
        out.println("</html>");   
		ServletConfig sc = this.getServletConfig();
		System.out.println(single++);
//		resp.sendError(404, "没有消息");
		InputStream in = req.getInputStream();
		System.out.println("resp:addDateHeader");
		resp.addDateHeader("1", 20140310);
		resp.addDateHeader("1", 20140311);
		resp.addDateHeader("1", 20140312);
		ServletContext application = this.getServletContext();
		application.setAttribute("a", "b");
		System.out.println(application.getAttribute("a"));
		byte[] buf = new byte[1024];
		int length = 0;
		String str = "";
		resp.setLocale(Locale.CHINA);
//		if(req.getSession().getAttribute("b")!=null) {
//			resp.addCookie(new Cookie("b", "b"));
//		} else {
//			resp.addCookie(new Cookie("a", "a"));
//		}
//		req.getSession().setAttribute("b", "b");
		
		while((length=in.read(buf))!=-1) {
			str+=new String(buf,0,length);
		}
		System.out.println("TestServletDoPost---30:"+str);
	//	HttpSession session=req.getSession();
	//	session.setAttribute("1", "1");
		
		//测试PrintWriter
        resp.setContentType("text/html");    
        PrintWriter out = resp.getWriter();    
        out.println("<html>");    
        out.println("<head>");    
        out.println("<title>Hello World</title>");    
        out.println("</head>");    
        out.println("<body>");    
        out.println("<h1>Hello World!</h1>");    
        out.println("</body>");    
        out.println("</html>");   
		
		//测试outputstream
//		OutputStream os = resp.getOutputStream();
//		os.write(new String("21234").getBytes());
//		System.out.println("finish");
		
		
//		File f = new java.io.File("D:\\迅雷下载\\en_windows_10_multi-edition_version_1709_updated_sept_2017_x86_dvd_100090818.iso");
//		InputStream fis = new FileInputStream(f);
//		byte[] buf = new byte[1024];
//		while(fis.read(buf)!=-1) {
//			os.write(buf);
//			
//		}

//		try {
//			Thread.sleep(1000*60);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//super.doGet(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	
		this.doGet(req, resp);
		
		//super.doPost(req, resp);
	}
}
