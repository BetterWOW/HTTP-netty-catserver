package com.zgx.http.imp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

import com.zgx.catserver.connector.SimpleInputStream;
import com.zgx.http.HTTPRequestData;
import com.zgx.http.HTTPResponseData;
import com.zgx.http.servlet.engine.ServletContextManager;
import com.zgx.http.session.SessionManager;
import com.zgx.http.structure.Parameter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponse;

public class Request implements HttpServletRequest {
	private Cookie[] cookies = null;
	private HTTPRequestData httpRequestData = null;
	private int port = 0;
	private HashMap<String, Object> attributes = new HashMap<String, Object>();
	private Parameter parameter = new Parameter();
	private HashMap<String, String[]> parameterMap = new HashMap<String, String[]>();
	private ServletInputStream inputStream = null;
	private String characterEncoding = Charset.defaultCharset().name();
	private ChannelHandlerContext ctx = null;
	private ServletContext servletContext = null;
	private HTTPResponseData httpResponseData = null;

	/**
	 * 构造器
	 * 
	 * @param httpRequestData
	 */
	public Request(HTTPRequestData httpRequestData, ChannelHandlerContext ctx, HTTPResponseData httpResponseData) {
		// TODO Auto-generated constructor stub
		this.httpRequestData = httpRequestData;
		this.ctx = ctx;
		this.httpResponseData = httpResponseData;

		this.port = Integer.parseInt(httpRequestData.getRequestHead("Host").split(":")[1]);
		this.servletContext = ServletContextManager
				.getContext("/" + this.httpRequestData.getRequestLine().getURL().split("/")[1]);
		// 将数据放入parameter
		// get请求方式
		if (this.httpRequestData.getRequestLine().getMethod().equals("GET")) {
			String URL = httpRequestData.getRequestLine().getURL();
			String paramStr = URL.substring(URL.indexOf("?") + 1, URL.length());
			String[] params = paramStr.split("&");
			for (int i = 0; i < params.length; i++) {
				if (params[i].contains("=")) {
					String[] param = params[i].split("=");
					//System.out.println(params[i]);
					if (param.length > 1) {
						this.parameter.put(param[0], param[1]);
					} else {
						String paramKey = param[0];
						param = new String[2];
						param[0] = paramKey;
						param[1] = "";
						this.parameter.put(param[0], "");
					}
					// 加入到parameterMap
					ArrayList<String> values = new ArrayList<String>();
					String[] valuesArray = parameterMap.get(param[0]);
					if (valuesArray != null) {
						values = new ArrayList<String>(Arrays.asList(valuesArray));
					}
					values.add(param[1]);
					valuesArray = new String[values.size()];
					System.out.println(valuesArray);
					values.toArray(valuesArray);
					this.parameterMap.put(param[0], valuesArray);

				} else {
					this.parameter.put(params[i], "");
				}
			}

		}
		// Post请求方式
		//System.out.println(this.httpRequestData.getRequestLine().getMethod());
		if (this.httpRequestData.getRequestLine().getMethod().equals("POST")) {
			String contentType = this.httpRequestData.getRequestHead(HTTPRequestData.CONTENT_TYPE);
			// 这里暂时只解析“APPLICATION/X-WWW-FORM-URLENCODED”类型的内容
			System.out.println(contentType);
			if (contentType.equals("APPLICATION/X-WWW-FORM-URLENCODED")) {
				String content = this.httpRequestData.getContent();
				String[] params = content.split("&");
				for (int i = 0; i < params.length; i++) {
					if (params[i].contains("=")) {
						String[] param = params[i].split("=");
						this.parameter.put(param[0], param[1]);
						// 加入到parameterMap
						ArrayList<String> values = new ArrayList<String>();
						String[] valuesArray = parameterMap.get(param[0]);
						if (valuesArray != null) {
							values = new ArrayList<String>(Arrays.asList(valuesArray));
						}
						values.add(param[1]);
						valuesArray = new String[values.size()];
						values.toArray(valuesArray);
						this.parameterMap.put(param[0], valuesArray);
					} else {
						this.parameter.put(params[i], "");
					}
				}

			}
		}

		// 初始化inputStream
		ByteBuf buf = Unpooled.buffer();
		buf.writeBytes(this.httpRequestData.getContent().getBytes());
		inputStream = new SimpleInputStream(buf);

		// 初始化cookies
		String cookiesStr = this.getHeader(HTTPRequestData.COOKIE);
		// System.out.println(cookiesStr);
		if (cookiesStr != null) {
			String[] cookiesStrs = cookiesStr.split(";");
			this.cookies = new Cookie[cookiesStrs.length];
			for (int i = 0; i < cookiesStrs.length; i++) {
				String[] cookieStr = cookiesStrs[i].split("=");
				this.cookies[i] = new Cookie(cookieStr[0].trim(), cookieStr[1]);
			}

		}

	}

	public Object getAttribute(String name) {
		// TODO Auto-generated method stub
		return attributes.get(name);
	}

	public Enumeration<String> getAttributeNames() {
		// TODO Auto-generated method stub

		return new Enumeration<String>() {
			private Object[] dataArray = attributes.keySet().toArray();
			private int count = 0;
			private int length = dataArray.length;

			public boolean hasMoreElements() {
				// TODO Auto-generated method stub
				return (this.count < this.length);
			}

			public String nextElement() {
				// TODO Auto-generated method stub
				return (String) dataArray[count++];
			}
		};
	}

	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return this.characterEncoding;
	}

	public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		// 将内容的编码格式修改
		String oldReqContent = this.httpRequestData.getContent();
		System.out.println("Request.setCharacterEncoding(String env)" + oldReqContent.length());
		String newReqContent = new String(oldReqContent.getBytes(), env);
		// 将URL的编码格式修改
		String oldURL = this.httpRequestData.getRequestLine().getURL();
		String newURL = new String(oldURL.getBytes(), env);
		try {
			this.httpRequestData.setContent(newReqContent);
			this.httpRequestData.getRequestLine().setURL(newURL);
			// 将parameter的编码格式修改
			Parameter oldParameter = this.parameter;
			Parameter newParameter = new Parameter();
			for (Entry<String, String> entry : oldParameter.entrySet()) {
				String newKey = new String(entry.getKey().getBytes(), env);
				String newValue = new String(entry.getValue().getBytes(), env);
				newParameter.put(newKey, newValue);
			}
			HashMap<String, String[]> newParameterMap = new HashMap<String, String[]>();
			for (Entry<String, String[]> entry : this.parameterMap.entrySet()) {
				String newKey = new String(entry.getKey().getBytes(), env);
				String[] oldValue = entry.getValue();
				String[] newValue = new String[oldValue.length];
				for (int i = 0; i < oldValue.length; i++) {
					newValue[i] = new String(oldValue[i].getBytes(), env);
				}
				newParameterMap.put(newKey, newValue);
			}
			this.parameter = newParameter;
			this.parameterMap = newParameterMap;

			// 初始化cookies
			if (this.getHeader(HTTPRequestData.COOKIE) != null) {
				String cookiesStr = this.getHeader(HTTPRequestData.COOKIE).trim();
				String[] cookiesStrs = cookiesStr.split(";");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * 
		 * 其它情况
		 */
		this.characterEncoding = env;

	}

	public int getContentLength() {
		// TODO Auto-generated method stub
		int length = Integer.parseInt(this.httpRequestData.getRequestHead(HTTPRequestData.CONTENT_LENGTH));
		return length;
	}

	public long getContentLengthLong() {
		// TODO Auto-generated method stub
		long length = Long.parseLong(this.httpRequestData.getRequestHead(HTTPRequestData.CONTENT_LENGTH));
		return length;
	}

	public String getContentType() {
		// TODO Auto-generated method stub
		return this.httpRequestData.getRequestHead(HTTPRequestData.CONTENT_TYPE);
	}

	public ServletInputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return this.inputStream;
	}

	public String getParameter(String name) {
		// TODO Auto-generated method stub
		System.out.println(name+this.parameter.get(name));
		return this.parameter.get(name);
	}

	public Enumeration<String> getParameterNames() {
		// TODO Auto-generated method stub
		return new Enumeration<String>() {
			private Object[] dataArray = parameter.keySet().toArray();
			private int count = 0;
			private int length = dataArray.length;

			public boolean hasMoreElements() {
				// TODO Auto-generated method stub
				return (this.count < this.length);
			}

			public String nextElement() {
				// TODO Auto-generated method stub
				return (String) dataArray[count++];
			}
		};
	}

	public String[] getParameterValues(String name) {
		// TODO Auto-generated method stub
		return this.parameterMap.get(name);
	}

	public Map<String, String[]> getParameterMap() {
		// TODO Auto-generated method stub
		return this.parameterMap;
	}

	public String getProtocol() {
		// TODO Auto-generated method stub
		return this.httpRequestData.getRequestLine().getProtocolVersion();
	}

	public String getScheme() {
		// TODO Auto-generated method stub
		return this.httpRequestData.getRequestLine().getProtocolVersion();
	}

	public String getServerName() {
		// TODO Auto-generated method stub
		return "catserver";
	}

	public int getServerPort() {
		// TODO Auto-generated method stub
		return this.port;
	}

	public BufferedReader getReader() throws IOException {
		// TODO Auto-generated method stub
		return null;
		/**
		 * 待实现
		 */
	}

	public String getRemoteAddr() {
		// TODO Auto-generated method stub
		return this.ctx.channel().remoteAddress().toString();
	}

	public String getRemoteHost() {
		// TODO Auto-generated method stub
		return this.ctx.channel().remoteAddress().toString().split(":")[0];
	}

	public void setAttribute(String name, Object o) {
		// TODO Auto-generated method stub
		this.attributes.put(name, o);

	}

	public void removeAttribute(String name) {
		// TODO Auto-generated method stub
		this.attributes.remove(name);

	}

	public Locale getLocale() {
		// TODO Auto-generated method stub
		return Locale.getDefault();
	}

	public Enumeration<Locale> getLocales() {
		// TODO Auto-generated method stub
		return new Enumeration<Locale>() {
			private Locale[] dataArray = Locale.getAvailableLocales();
			private int count = 0;
			private int length = dataArray.length;

			public boolean hasMoreElements() {
				// TODO Auto-generated method stub
				return (this.count < this.length);
			}

			public Locale nextElement() {
				// TODO Auto-generated method stub
				return dataArray[count++];
			}
		};
	}

	public boolean isSecure() {
		// TODO Auto-generated method stub
		return false;
	}

	public RequestDispatcher getRequestDispatcher(String path) {
		// TODO Auto-generated method stub
		this.getServletContext();
		if (!(path.trim().substring(0, 1).equals("/"))) {
			path = "/" + path;
		}
		path = path.trim();
		String servletName = ServletContextManager.getServletName(this.getContextPath() + path);
		// System.out.println(sc);//测试sc是否与jsp的sc一致
		// 如果没有匹配的servlet，使用defaultHttpServlet执行
		this.httpRequestData.getRequestLine().setURL(this.getContextPath() + path);
		return new SimpleRequestDispatcher(servletName);

	}

	public String getRealPath(String path) {
		// TODO Auto-generated method stub
		return null;
		/**
		 * 待实现
		 */
	}

	public int getRemotePort() {
		// TODO Auto-generated method stub
		return Integer.parseInt(this.ctx.channel().remoteAddress().toString().split(":")[1]);
	}

	public String getLocalName() {
		// TODO Auto-generated method stub
		return this.ctx.channel().localAddress().toString().split(":")[0];
	}

	public String getLocalAddr() {
		// TODO Auto-generated method stub
		return this.ctx.channel().localAddress().toString();
	}

	public int getLocalPort() {
		// TODO Auto-generated method stub
		return Integer.parseInt(this.ctx.channel().localAddress().toString().split(":")[1]);
	}

	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return this.servletContext;
	}

	public AsyncContext startAsync() throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse)
			throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isAsyncStarted() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isAsyncSupported() {
		// TODO Auto-generated method stub
		return false;
	}

	public AsyncContext getAsyncContext() {
		// TODO Auto-generated method stub
		return null;
	}

	public DispatcherType getDispatcherType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAuthType() {
		// TODO Auto-generated method stub
		return null;
	}

	public Cookie[] getCookies() {
		// TODO Auto-generated method stub
		return this.cookies;
	}

	public long getDateHeader(String name) {
		// TODO Auto-generated method stub
		long dateLong = -1;
		if (this.httpRequestData.getRequestHead(name) != null) {
			dateLong = Date.parse(this.httpRequestData.getRequestHead(name));
		}
		return dateLong;
	}

	public String getHeader(String name) {
		// TODO Auto-generated method stub
		return this.httpRequestData.getRequestHead(name);
	}

	public Enumeration<String> getHeaders(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public Enumeration<String> getHeaderNames() {
		// TODO Auto-generated method stub
		return new Enumeration<String>() {

			private Object[] dataArray = httpRequestData.getRequestHead().keySet().toArray();
			private int count = 0;
			private int length = dataArray.length;

			public boolean hasMoreElements() {
				// TODO Auto-generated method stub
				return (this.count < this.length);
			}

			public String nextElement() {
				// TODO Auto-generated method stub
				return (String) dataArray[count++];
			}
		};
	}

	public int getIntHeader(String name) {
		// TODO Auto-generated method stub
		if (this.httpRequestData.getRequestHead(name) != null)
			return Integer.parseInt(this.httpRequestData.getRequestHead(name));
		else
			return -1;
	}

	public String getMethod() {
		// TODO Auto-generated method stub
		return this.httpRequestData.getRequestLine().getMethod();
	}

	public String getPathInfo() {
		// TODO Auto-generated method stub
		return null;
		/**
		 * 待实现
		 */
	}

	public String getPathTranslated() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getContextPath() {
		// TODO Auto-generated method stub
		return this.servletContext.getContextPath();

	}

	public String getQueryString() {
		// TODO Auto-generated method stub
		return this.httpRequestData.getRequestLine().getURL().split("?")[1];
	}

	public String getRemoteUser() {
		// TODO Auto-generated method stub
		return null;
		/**
		 * 待实现
		 */
	}

	public boolean isUserInRole(String role) {
		// TODO Auto-generated method stub
		return false;
	}

	public Principal getUserPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRequestedSessionId() {
		// TODO Auto-generated method stub
		return null;
		/**
		 * 待实现
		 */
	}

	public String getRequestURI() {
		// TODO Auto-generated method stub
		if (this.httpRequestData.getRequestLine().getURL().contains("?")) {
			System.out.println(
					"Request. getRequestURI()" + this.httpRequestData.getRequestLine().getURL().split("\\?")[0]);
			//System.out.println("Request. getRequestURI()" + this.getParameter("username"));
			return this.httpRequestData.getRequestLine().getURL().split("\\?")[0];

		} else
			return this.httpRequestData.getRequestLine().getURL();
	}

	public StringBuffer getRequestURL() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getServletPath() {
		// TODO Auto-generated method stub
		return null;
	}

	public HttpSession getSession(boolean create) {
		// TODO Auto-generated method stub
		HttpSession result = null;
		String sessionID = null;
		if (cookies != null) {
			for (int i = 0; i < this.cookies.length; i++) {
				if (this.cookies[i].getName().trim().equals("sessionID")) {
					sessionID = this.cookies[i].getValue().trim();
				}
			}
		}
		if (sessionID == null || SessionManager.getHttpSessionByID(sessionID) == null) {
			if (create) {
				result = SessionManager.createHttpSession(this.servletContext);
				this.httpResponseData.addCookie(new Cookie("sessionID", result.getId()));
				// System.out.println( "Request.getSession(boolean create)");
			}
		} else {
			result = SessionManager.getHttpSessionByID(sessionID);
			// System.out.println( "Request.getSession(boolean create)");
		}

		return result;
	}

	public HttpSession getSession() {
		// TODO Auto-generated method stub
		return this.getSession(true);

	}

	public String changeSessionId() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isRequestedSessionIdValid() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isRequestedSessionIdFromCookie() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isRequestedSessionIdFromURL() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isRequestedSessionIdFromUrl() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
		// TODO Auto-generated method stub
		return false;
	}

	public void login(String username, String password) throws ServletException {
		// TODO Auto-generated method stub

	}

	public void logout() throws ServletException {
		// TODO Auto-generated method stub

	}

	public Collection<Part> getParts() throws IOException, ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	public Part getPart(String name) throws IOException, ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
		// TODO Auto-generated method stub
		return null;
	}

}
