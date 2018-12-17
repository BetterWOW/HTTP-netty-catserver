package com.zgx.http.servlet.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;
import javax.servlet.http.HttpServlet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.SAXException;

import com.sun.rowset.internal.WebRowSetXmlReader;
import com.zgx.http.HTTPRequestData;
import com.zgx.http.HTTPResponseData;
import com.zgx.http.classloader.WebAppClassLoader;
import com.zgx.http.imp.Request;
import com.zgx.http.imp.Response;
import com.zgx.http.servlet.DefaultHttpServlet;
import com.zgx.http.servlet.engine.ServletContextManager;
import com.zgx.tools.FileTools;
import com.zgx.tools.LoaderClassTools;
import com.zgx.tools.PathTools;

public class SimpleServletContext implements ServletContext {

	/**
	 * webAppPath:webapp的路径 contextPath：ContextPath webAppDoc：web.xml的doc
	 */
	private String webAppPath = null;
	private String contextPath = null;
	private Document webAppDoc = null;
	private ClassLoader classLoader = null;
	// private ClassLoader classLoader = null;
	private Map<String, String> servletClassNameMap = new HashMap<String, String>();
	private Map<String, Servlet> servletMap = new HashMap<String, Servlet>();
	private Map<String, String> initParams = new HashMap<String, String>();
	private Map<String, Object> attributeMap = new HashMap<String, Object>();

	/**
	 * 构造器
	 * 
	 * @param webAppPath
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws JDOMException
	 */
	public SimpleServletContext(String webAppPath)
			throws ParserConfigurationException, SAXException, IOException, JDOMException {
		// TODO Auto-generated constructor stub
		this.webAppPath = webAppPath;
		this.contextPath = webAppPath.substring(webAppPath.lastIndexOf("/"));
		System.out.println("初始化WebApp："+contextPath);
		SAXBuilder sb = new SAXBuilder();
		this.webAppDoc = sb.build(new File(this.webAppPath + "/" + "WEB-INF" + "/" + "web.xml"));
		// this.classLoader = new WebAppClassLoader(webAppPath+"/"+"WEB-INF");
		Element element = this.webAppDoc.getRootElement();
		Element initParamsE = element.getChild("context-param");
		if (initParamsE != null) {
			List<Element> initParamsList = initParamsE.getChildren();
			for (Element e : initParamsList) {
				this.initParams.put(e.getChild("param-name").getValue(), e.getChild("param-value").getValue());
			}
		}
		
		//创建jsp对应servlet的包文件，确保classloader能加载到新建的类
		FileTools.createDir(Class.class.getClass().getResource("/").getPath()+"work"+contextPath+"/com/zgx/jsp");
	
		
		//servlet的classpath和jsp对应的serlet的classpath和lib
		String classPath = webAppPath + "/WEB-INF/classes";
		String webappUri = webAppPath.substring(webAppPath.lastIndexOf("/"));
		String jspServletClassPath =  Class.class.getClass().getResource("/").getPath()+"work/"+webappUri;
		String webappLib = webAppPath + "/WEB-INF/lib";
//		System.out.println(webappLib+"---------");
		File file1 = new File(classPath);
		URL url1 = file1.toURI().toURL();
		File file2 = new File(jspServletClassPath);
		URL url2 = file2.toURI().toURL();
		this.classLoader = new URLClassLoader(new URL[] { url1,url2});
		
		//使用SystemClassLoader加载web项目下的lib包
		try {
			LoaderClassTools.loadJarsInDir(webappLib);
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//初始化一个default的servlet
		this.servletClassNameMap.put("defaultHttpServlet", "com.zgx.http.servlet.DefaultServlet");
		HttpServlet defaultHttpServlet = new DefaultHttpServlet();
		try {
			defaultHttpServlet.init(new SimpleServletConfig("defaultHttpServlet", this, new HashMap<String, String>()));
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.servletMap.put("com.zgx.http.servlet.DefaultServlet",defaultHttpServlet);
		
		

	}

	public String getContextPath() {
		// TODO Auto-generated method stub
		return this.contextPath;
	}

	public ServletContext getContext(String uripath) {
		// TODO Auto-generated method stub
		return ServletContextManager.getContext(uripath);
	}

	public int getMajorVersion() {
		// TODO Auto-generated method stub
		return 3;
	}

	public int getMinorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getEffectiveMajorVersion() {
		// TODO Auto-generated method stub
		return 3;
	}

	public int getEffectiveMinorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getMimeType(String file) {
		// TODO Auto-generated method stub
		return null;/**
					 * 待实现
					 */
	}

	public Set<String> getResourcePaths(String path) {
		// TODO Auto-generated method stub
		return null;/**
					 * 
					 */
	}

	public URL getResource(String path) throws MalformedURLException {
		// TODO Auto-generated method stub
		return null;/**
					 * 待实现
					 */
	}

	public InputStream getResourceAsStream(String path) {
		// TODO Auto-generated method stub
		File file = new File(path);
		InputStream in = null;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return in;
	}

	public RequestDispatcher getRequestDispatcher(String path) {
		// TODO Auto-generated method stub
		return null;/**
					 * 待实现
					 */
	}

	public RequestDispatcher getNamedDispatcher(String name) {
		// TODO Auto-generated method stub
		return null;/**
					 * 待实现
					 */
	}
	public Servlet getServlet(String name) throws ServletException {
		// TODO Auto-generated method stub
		Servlet result = null;
		if ((result = this.servletMap.get(this.servletClassNameMap.get(name))) == null) {

			Element element = this.webAppDoc.getRootElement();
			List<Element> servletList = element.getChildren("servlet");
			for (Element e : servletList) {
				Element servletName = e.getChild("servlet-name");
				if (servletName.getValue().trim().equals(name.trim())) {
					String className = e.getChild("servlet-class").getValue();
					HashMap<String, String> servletInitParams = new HashMap<String, String>();
					List<Element> paramsList =  e.getChildren("init-param");
					for (Element e1 : paramsList) {
						servletInitParams.put(e1.getChild("param-name").getValue(),
								e1.getChild("param-value").getValue());
					}

					SimpleServletConfig servletConfig = new SimpleServletConfig(className, this, servletInitParams);
					try {

						Class<?> clazz = this.classLoader.loadClass(className);
						Servlet servlet = (Servlet) clazz.newInstance();
						servlet.init(servletConfig);
						this.servletClassNameMap.put(name, className);
						this.servletMap.put(className, servlet);
						result = servlet;
						//测试代码
//						HTTPRequestData httpRequestData = HTTPRequestData.getHTTPRequestDataInstance("Post /favicon.ico HTTP/1.1\r\n" + 
//								"Host: 127.0.0.1:8080\r\n" + 
//								"Connection: keep-alive\r\n" + 
//								"User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36\r\n" + 
//								"Accept: image/webp,image/apng,image/*,*/*;q=0.8\r\n" + 
//								"Referer: http://127.0.0.1:8080/\r\n" + 
//								"Accept-Encoding: gzip, deflate, br\r\n" +
//								"content-length: 10\r\n"+
//								"content-type:APPLICATION/X-WWW-FORM-URLENCODED\r\n"+
//								"Accept-Language: zh-CN,zh;q=0.9");
//						try {
//							httpRequestData.setContent("0123456789");
//						} catch (Exception e2) {
//							// TODO Auto-generated catch block
//							e2.printStackTrace();
//						}
//						result.service(new Request(httpRequestData, null), new Response(null, null, new HTTPResponseData()));
					} catch (ClassNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} catch (InstantiationException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} catch (IllegalAccessException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
//					 catch (IOException e2) {
//						// TODO Auto-generated catch block
//						e2.printStackTrace();
//					} catch (ExecutionException e2) {
//						// TODO Auto-generated catch block
//						e2.printStackTrace();
//					}

				}
			}
		}
		return result;
	}

	public Enumeration<Servlet> getServlets() {
		// TODO Auto-generated method stub
		return new Enumeration<Servlet>() {
			private Object[] dataArray =  servletMap.keySet().toArray();
			private int count = 0;
			private int length = dataArray.length;

			public boolean hasMoreElements() {
				// TODO Auto-generated method stub
				return (this.count < this.length);
			}

			public Servlet nextElement() {
				// TODO Auto-generated method stub
				return servletMap.get((String)dataArray[count++]);
			}
		};
	}

	public Enumeration<String> getServletNames() {
		// TODO Auto-generated method stub
		return new Enumeration<String>() {
			private Object[] dataArray =  servletMap.keySet().toArray();
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

	public void log(String msg) {
		// TODO Auto-generated method stub

	}

	public void log(Exception exception, String msg) {
		// TODO Auto-generated method stub

	}

	public void log(String message, Throwable throwable) {
		// TODO Auto-generated method stub

	}

	public String getRealPath(String path) {
		// TODO Auto-generated method stub
		return this.webAppPath+path;
	}

	public String getServerInfo() {
		// TODO Auto-generated method stub
		return "netty-catserver 1.0.0";
	}

	public String getInitParameter(String name) {
		// TODO Auto-generated method stub
		return this.initParams.get(name);
	}

	public Enumeration<String> getInitParameterNames() {
		// TODO Auto-generated method stub
		return new Enumeration<String>() {
			private Object[] dataArray =  initParams.keySet().toArray();
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

	public boolean setInitParameter(String name, String value) {
		// TODO Auto-generated method stub
		this.initParams.put(name, value);
		return this.initParams.get(name) != null;
	}

	public Object getAttribute(String name) {
		// TODO Auto-generated method stub
		return this.attributeMap.get(name);
	}

	public Enumeration<String> getAttributeNames() {
		// TODO Auto-generated method stub
		return new Enumeration<String>() {
			private Object[] dataArray =  attributeMap.keySet().toArray();
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

	public void setAttribute(String name, Object object) {
		// TODO Auto-generated method stub
		this.attributeMap.put(name, object);

	}

	public void removeAttribute(String name) {
		// TODO Auto-generated method stub
		this.attributeMap.remove(name);

	}

	public String getServletContextName() {
		// TODO Auto-generated method stub
		System.out.println(this.getClass().getName());
		// return this.webAppDoc.("display-name").item(0).getTextContent();
		Element element = this.webAppDoc.getRootElement();
		Element name = element.getChild("display-name");
		return name.getValue();
		/**
		 * 待实现
		 */
	}

	public Dynamic addServlet(String servletName, String className) {
		// TODO Auto-generated method stub
		this.servletClassNameMap.put(servletName, className);
		Class<?> clazz;
		try {
			clazz = this.classLoader.loadClass(className);
			this.servletMap.put(className, (Servlet) clazz.newInstance());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Dynamic addServlet(String servletName, Servlet servlet) {
		// TODO Auto-generated method stub
		this.servletClassNameMap.put(servletName, servlet.getClass().getName());
		this.servletMap.put(servlet.getClass().getName(), servlet);
		return null;
	}

	public Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
		// TODO Auto-generated method stub
		Servlet servlet = null;
		try {
			servlet = (Servlet) servletClass.newInstance();
			this.servletClassNameMap.put(servletName, servlet.getClass().getName());
			this.servletMap.put(servlet.getClass().getName(), servlet);
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;
	}

	public <T extends Servlet> T createServlet(Class<T> clazz) throws ServletException {
		// TODO Auto-generated method stub
		T t = null;
		try {
			t = clazz.newInstance();
			this.addServlet(t.getClass().getName(), t);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
	}

	public ServletRegistration getServletRegistration(String servletName) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, ? extends ServletRegistration> getServletRegistrations() {
		// TODO Auto-generated method stub
		return null;
	}

	public javax.servlet.FilterRegistration.Dynamic addFilter(String filterName, String className) {
		// TODO Auto-generated method stub
		return null;
	}

	public javax.servlet.FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	public javax.servlet.FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T extends Filter> T createFilter(Class<T> clazz) throws ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	public FilterRegistration getFilterRegistration(String filterName) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
		// TODO Auto-generated method stub
		return null;
	}

	public SessionCookieConfig getSessionCookieConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {
		// TODO Auto-generated method stub

	}

	public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addListener(String className) {
		// TODO Auto-generated method stub

	}

	public <T extends EventListener> void addListener(T t) {
		// TODO Auto-generated method stub

	}

	public void addListener(Class<? extends EventListener> listenerClass) {
		// TODO Auto-generated method stub

	}

	public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	public JspConfigDescriptor getJspConfigDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	public ClassLoader getClassLoader() {
		// TODO Auto-generated method stub
		return this.classLoader;
	}

	public void declareRoles(String... roleNames) {
		// TODO Auto-generated method stub

	}

	public String getVirtualServerName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	// 测试SimpleServletContext的功能
		public static void main(String[] args) {
			String path = Class.class.getClass().getResource("/").getPath() + "webapps/ROOT";// System.getProperty("user.dir");
			System.out.println(path);

			// System.out.println(System.getProperty("user.dir"));
			File file = new File(path);
			String[] fileList = file.list();
			for (int i = 0; i < fileList.length; i++) {
				System.out.println(fileList[i]+"====="+i);
				System.out.println(file.toURI().getPath());
			}
			try {
				SimpleServletContext ssc = null;
				System.out.println((ssc = new SimpleServletContext(path)).getServletContextName());
				ssc.getServlet("servlet-name");
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JDOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

}
