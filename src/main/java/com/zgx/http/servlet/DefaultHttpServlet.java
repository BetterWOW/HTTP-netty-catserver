package com.zgx.http.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zgx.http.HTTPResponseData;
import com.zgx.http.HTTPResponseData.ResponseHead;
import com.zgx.http.jsp.paser.JspPaser;
import com.zgx.http.servlet.application.SimpleServletConfig;
import com.zgx.http.servlet.engine.ServletContextManager;

/**
 * 默认servlet用于返回静态资源与及加载jspserlet，并初始化jspserlet
 * @author userzgx
 *
 */
public class DefaultHttpServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		System.out.println(cookie.getName()+"-------------------------------------");
		String uri = req.getRequestURI();
		String webappUri = "/" + uri.split("/")[1];
		String realUriPath = Class.class.getClass().getResource("/").getPath() + "webapps" + uri;
		File file = new File(realUriPath);
		String suffix ="";
		if(uri.contains("."))
			suffix= uri.substring(uri.lastIndexOf(".")).trim().toLowerCase();
		OutputStream os = resp.getOutputStream();
		if (file.exists() && file.isFile()) {
			FileInputStream fileInputStream = new FileInputStream(file);
			if (suffix.equals(".html")) {
				resp.setContentType(HTTPResponseData.ContentType.TEXT_HTML);
				//resp.setContentLengthLong(file.length());
				this.writeFileToResp(os, fileInputStream);
				//System.out.println("DefaultHttpServlet.doPost-html");

			} else if (suffix.equals(".css")) {
				//resp.setContentLengthLong(file.length());
				resp.setContentType(HTTPResponseData.ContentType.TEXT_CSS);
				resp.setContentLengthLong(file.length());
				this.writeFileToResp(os, fileInputStream);

			} else if (suffix.equals(".js")) {
				resp.setContentType(HTTPResponseData.ContentType.APPLICATION_JS);
				resp.setContentLengthLong(file.length());
				this.writeFileToResp(os, fileInputStream);
			} else if (suffix.equals(".asp")) {
				resp.setContentType(HTTPResponseData.ContentType.TEXT_ASP);
				resp.setContentLengthLong(file.length());
				this.writeFileToResp(os, fileInputStream);
			} else if (suffix.equals(".txt")) {
				resp.setContentType(HTTPResponseData.ContentType.TEXT_PLAIN);
				resp.setContentLengthLong(file.length());
				this.writeFileToResp(os, fileInputStream);
			} else if (suffix.equals(".pdf")) {
				resp.setContentType(HTTPResponseData.ContentType.APPLICATION_PDF);
				resp.setContentLengthLong(file.length());
				this.writeFileToResp(os, fileInputStream);
			} else if (suffix.equals(".xml")) {
				resp.setContentType(HTTPResponseData.ContentType.APPLICATION_XML);
				resp.setContentLengthLong(file.length());
				this.writeFileToResp(os, fileInputStream);

			} else if (suffix.equals(".zip")) {
				resp.setContentType(HTTPResponseData.ContentType.APPLICATION_ZIP);
				resp.setContentLengthLong(file.length());
				this.writeFileToResp(os, fileInputStream);

			} else if (suffix.equals(".mp4")) {
				resp.setContentType(HTTPResponseData.ContentType.VIDEO_MPEG4);
				resp.setContentLengthLong(file.length());
				this.writeFileToResp(os, fileInputStream);

			} else if (suffix.equals(".jpe") || suffix.equals(".jpeg") || suffix.equals(".jpg")) {
				resp.setContentType(HTTPResponseData.ContentType.IMAGE_JPEG);
				resp.setContentLengthLong(file.length());
				this.writeFileToResp(os, fileInputStream);
				
			} else if (suffix.equals(".png")) {
				resp.setContentType(HTTPResponseData.ContentType.IMAGE_PNG);
				resp.setContentLengthLong(file.length());
				this.writeFileToResp(os, fileInputStream);

			} else if (suffix.equals(".jsp")) {
				ServletContext servletContext = req.getServletContext();
				
				//设置jsp对应servlet的uri和name，这里设置为相同即可
				
				
				//生成jsp转为java代码方法paserJspTojava的参数
				String jspPath = realUriPath;
				String jsp_servlet_Name =uri.substring(webappUri.length()).replaceAll("/", "_");
				//解析jsp为java并编译java代码为class文件
				JspPaser.paserJspTojava(jspPath, webappUri, jsp_servlet_Name);
				//使用Servlet的加载器加载jsp对应的servlet
				ClassLoader classLoader = servletContext.getClassLoader();
			    
				try {
//					System.out.println(classLoader);
//					System.out.println(jsp_servlet_Name);
					jsp_servlet_Name="com.zgx.jsp."+jsp_servlet_Name.replaceAll("\\.", "_");
//					System.out.println(jsp_servlet_Name);
					Class<?> clazz = classLoader.loadClass(jsp_servlet_Name);
//					System.out.println(clazz);
					HttpServlet httpServlet = (HttpServlet)clazz.newInstance();
					httpServlet.init(new SimpleServletConfig(uri, req.getServletContext(), new HashMap<String, String>()));
					//将jsp对应的servlet名称设置为uri，即uri和servlet名称相同
					ServletContextManager.setUriMapping(uri, uri);
					servletContext.addServlet(uri, httpServlet);
//    				System.out.println(servletContext.getServlet(uri)+"128888888888"+uri+servletContext);
					httpServlet.service(req, resp);
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
				
				//ServletContextManager.setUriMapping(uri, uri);
			} else {
				resp.setContentType(HTTPResponseData.ContentType.APPLICATION_OCTET_STREAM);
				resp.setContentLengthLong(file.length());
				this.writeFileToResp(os, fileInputStream);
			}

		}	else {
			//发送没有找到资源
			resp.sendError(HTTPResponseData.StatusCode.NOT_FOUND, "<p>\n" + 
					"<font size=\"10\" face=\"Times\">\n" + 
					"404 Not Found.\n" + 
					"</font>\n" + 
					"</p>");
			
		}
	}

	private void writeFileToResp(OutputStream respOut, FileInputStream fileInputStream) throws IOException {
		byte[] bytes = new byte[4096];
		long fileLength = 0;
		int readLength = 0;
		int indexTime = 0;
		while ((readLength = fileInputStream.read(bytes)) != -1) {
			respOut.write(bytes,0,readLength);
			fileLength+=readLength;
			
		}
		fileInputStream.close();
	}

	public static void main(String[] args) {
		File file = new File("E:\\Users\\userzgx\\Desktop\\web-test\\register.jsp");
		System.out.println(file.length());
		System.out.println("dsfgas.sdfas".indexOf("."));
		System.out.println(".*");
	}
}
