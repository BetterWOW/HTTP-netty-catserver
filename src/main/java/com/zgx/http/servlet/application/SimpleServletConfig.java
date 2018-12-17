package com.zgx.http.servlet.application;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public class SimpleServletConfig implements ServletConfig{
	private String servletName ;
	private ServletContext servletContext;
	private Map< String, String> initParams = new HashMap<String, String>();
	public SimpleServletConfig(String servletName,ServletContext servletContext,Map<String, String> initParams) {
		// TODO Auto-generated constructor stub
		this.servletName = servletName;
		this.servletContext = servletContext;
		this.initParams = initParams;
	}
	
	public String getServletName() {
		// TODO Auto-generated method stub
		return this.servletName;
	}

	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return this.servletContext;
	}

	public String getInitParameter(String name) {
		// TODO Auto-generated method stub
		return this.initParams.get(name);
	}

	public Enumeration<String> getInitParameterNames() {
		// TODO Auto-generated method stub
		return new Enumeration<String>() {
			private Object[] dataArray = initParams.keySet().toArray();
			private int count = 0;
			private int length = dataArray.length;

			public boolean hasMoreElements() {
				// TODO Auto-generated method stub
				return (this.count < this.length);
			}

			public String nextElement() {
				// TODO Auto-generated method stub
				return ((String)dataArray[count++]);
			}
		};
	}

}
