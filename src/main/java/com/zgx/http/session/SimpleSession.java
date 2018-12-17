package com.zgx.http.session;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import com.zgx.tools.DateTimeTools;

public class SimpleSession implements HttpSession {
	private Map<String, Object> attribute = new HashMap<String, Object>();
	private long createTime = DateTimeTools.currentTimeLong();
	private String id ;
	private ServletContext servletContext = null;
	private int maxInactiveInterval = 60*10;
	public SimpleSession(long sessionId,ServletContext servletContext) {
		// TODO Auto-generated constructor stub
		this.id = createTime+"_"+sessionId;
		this.servletContext = servletContext;
	}
	public long getCreationTime() {
		// TODO Auto-generated method stub
		return this.createTime;
	}

	public String getId() {
		// TODO Auto-generated method stub
		return this.id;
	}

	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		return SessionManager.getLastAccessTimeMap(id);
	}

	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return this.servletContext;
	}

	public void setMaxInactiveInterval(int interval) {
		// TODO Auto-generated method stub
		this.maxInactiveInterval = interval;
	}

	public int getMaxInactiveInterval() {
		// TODO Auto-generated method stub
		return this.maxInactiveInterval;
	}

	public HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		return null;
		
	}

	public Object getAttribute(String name) {
		// TODO Auto-generated method stub
		return this.attribute.get(name);
	}

	public Object getValue(String name) {
		// TODO Auto-generated method stub
		return this.attribute.get(name);
	}

	public Enumeration<String> getAttributeNames() {
		// TODO Auto-generated method stub
		return new Enumeration<String>() {
			private Object[] dataArray =  attribute.keySet().toArray();
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

	public String[] getValueNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setAttribute(String name, Object value) {
		// TODO Auto-generated method stub
		this.attribute.put(name, value);
		
	}

	public void putValue(String name, Object value) {
		// TODO Auto-generated method stub
		this.attribute.put(name, value);

	}

	public void removeAttribute(String name) {
		// TODO Auto-generated method stub
		this.attribute.remove(name);

	}

	public void removeValue(String name) {
		// TODO Auto-generated method stub
		this.attribute.remove(name);

	}

	public void invalidate() {
		// TODO Auto-generated method stub
		SessionManager.removeSession(this);

	}

	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}

}
