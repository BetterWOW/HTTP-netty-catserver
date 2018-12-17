package com.zgx.http.servlet.engine;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import com.zgx.catserver.connector.CatServerBootstrap;
import com.zgx.http.HTTPRequestData;
import com.zgx.http.HTTPResponseData;
import com.zgx.http.imp.Request;
import com.zgx.http.imp.Response;
import com.zgx.http.session.SessionClearerThread;
import com.zgx.tools.FileTools;

public class EngineStart {
	public static void main(String[] args) {
		System.out.println("####开始初始化webapps目录下的webapp####");
		String webappsPath = Class.class.getClass().getResource("/").getPath() + "webapps";
		String[] webappPaths = FileTools.getFileDir(webappsPath);
		//初始化每个webapps对应的ServletContext
		ServletContextManager.initUriMapping(webappPaths);
		System.out.println("####已经初始化webapps目录下的webapp####");
		//启动session回收线程
		Thread sessionClearThread = new Thread(new SessionClearerThread());
		System.out.println("session管理线程已启动");
		sessionClearThread.start();
		//启动netty监听，开始服务
		try {
			new CatServerBootstrap(8080).start();
			System.out.println("服务器监听已启动，服务器开始服务，监听端口为："+8080);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
