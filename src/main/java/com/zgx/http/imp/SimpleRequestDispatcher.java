package com.zgx.http.imp;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import com.sun.org.apache.regexp.internal.recompile;
import com.zgx.http.servlet.engine.ServletContextManager;

public class SimpleRequestDispatcher implements RequestDispatcher  {

	private String servletName  = null;
	public SimpleRequestDispatcher(String servletName) {
		// TODO Auto-generated constructor stub
		this.servletName = servletName;
	}
	public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if(response.isCommitted()) {
			throw new IllegalStateException();
		}
		response.reset();
		Servlet servlet  =null;
		if (servletName == null) {
			servlet =  request.getServletContext().getServlet("defaultHttpServlet");
//			System.out.println(" CatserverRequestHandler --------73:defaultHttpServlet" + servlet);

		} else {
			// 获取一个servletName为servletName的servlet
			servlet = request.getServletContext().getServlet(servletName);
		}
		//System.out.println("forward");
		servlet.service(request, response);
		response.flushBuffer();
		
		
	}

	public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Servlet servlet  =null;
		if (servletName == null) {
			servlet =  request.getServletContext().getServlet("defaultHttpServlet");
			//System.out.println(" CatserverRequestHandler --------73:defaultHttpServlet" + servlet);

		} else {
			// 获取一个servletName为servletName的servlet
			servlet = request.getServletContext().getServlet(servletName);
		}
//		System.out.println("SimpleRequestDispatcher.include");
		servlet.service(request, response);
		//System.out.println(response.get);
	}

}
