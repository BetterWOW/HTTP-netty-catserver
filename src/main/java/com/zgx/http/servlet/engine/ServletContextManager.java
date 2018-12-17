package com.zgx.http.servlet.engine;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.SAXException;

import com.zgx.http.HTTPRequestData;
import com.zgx.http.HTTPResponseData;
import com.zgx.http.imp.Request;
import com.zgx.http.imp.Response;
import com.zgx.http.servlet.application.SimpleServletContext;
import com.zgx.tools.FileTools;
/**
 * 用于管理servletContext
 * @author userzgx
 *
 */
public class ServletContextManager {
	private static Map<String, ServletContext> contextMap = new HashMap<String, ServletContext>();
	private static Map<String, String> uriMapping = new HashMap<String, String>();
	private static boolean inited = false;
	public static void addContext(String uripath, ServletContext context) {
		contextMap.put(uripath, context);
	}

	public static void initUriMapping(String[] webappPaths) {
		//如果已经初始化直接返回
		if(inited) 
			return;
		for (int i = 0; i < webappPaths.length; i++) {

			String rootUri = webappPaths[i].substring(webappPaths[i].lastIndexOf("/"));
			// 设置contextMap
			try {
				ServletContext servletContext = new SimpleServletContext(webappPaths[i]);
				ServletContextManager.addContext(rootUri, servletContext);
			} catch (ParserConfigurationException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (SAXException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (JDOMException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			// 设置servletname和uri映射关系
			SAXBuilder sb = new SAXBuilder();
			Document webAppDoc = null;
			try {
				webAppDoc = sb.build(new File(webappPaths[i] + "/" + "WEB-INF" + "/" + "web.xml"));
			} catch (JDOMException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// this.classLoader = new WebAppClassLoader(webAppPath+"/"+"WEB-INF");
			Element element = webAppDoc.getRootElement();
			List<Element> servletMapingList = element.getChildren("servlet-mapping");
			for (Element e : servletMapingList) {
				uriMapping.put(rootUri + e.getChild("url-pattern").getValue(), e.getChild("servlet-name").getValue());
			}
			uriMapping.put(rootUri+"/defaultHttpServlet","defaultHttpServlet");
		}
		inited=true; 
	}

	public static ServletContext getContext(String uripath) {
		return contextMap.get(uripath);
	}

	public static void setUriMapping(String uri, String servletName) {
		uriMapping.put(uri, servletName);
	}

	public static String getServletName(String uri) {
		return uriMapping.get(uri);
	}

	// 测试代码
	public static void main(String[] args) {
		
		//获取所有webapps的路径
		String[] webappsPath = FileTools.getFileDir("/E:/eclipse-workspace/netty-catserver/target/classes/webapps");
		for (int i = 0; i < webappsPath.length; i++) {
			System.out.println(webappsPath[i]);
		}
		//初始化每个webapps对应的ServletContext
		ServletContextManager.initUriMapping(webappsPath);
		//获取一个Uri为/ROOT的ServletContext sc
		ServletContext sc = ServletContextManager.getContext("/ROOT");
		//模仿访问
		try {
			//获取一个uri为/ROOT/servletUrl的servlet
			Servlet servlet = sc.getServlet(ServletContextManager.getServletName("/ROOT/servletUrl"));
			HTTPRequestData httpRequestData = HTTPRequestData.getHTTPRequestDataInstance(
					"Post /favicon.ico HTTP/1.1\r\n" + "Host: 127.0.0.1:8080\r\n" + "Connection: keep-alive\r\n"
							+ "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36\r\n"
							+ "Accept: image/webp,image/apng,image/*,*/*;q=0.8\r\n"
							+ "Referer: http://127.0.0.1:8080/\r\n" + "Accept-Encoding: gzip, deflate, br\r\n"
							+ "content-length: 10\r\n" + "content-type:APPLICATION/X-WWW-FORM-URLENCODED\r\n"
							+ "Accept-Language: zh-CN,zh;q=0.9");
			try {
				httpRequestData.setContent("0123456789");
				servlet.service(new Request(httpRequestData, null,null), new Response(new HTTPResponseData(),null, null));
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(ServletContextManager.getServletName("/ROOT/servletUrl"));
	}

}
