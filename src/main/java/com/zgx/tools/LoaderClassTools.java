package com.zgx.tools;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class LoaderClassTools {
	public static void loadJarsInDir(String dirPath) throws MalformedURLException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		//System.out.println(dirPath);
		File dirFile = new File(dirPath);
		if(dirFile.isDirectory()) {
			String[] filesName = dirFile.list();
			for (int i = 0; i < filesName.length; i++) {
				URL url= new File(dirPath+"/"+filesName[i]).toURI().toURL();//将File类型转为URL类型，file为jar包路径  
				//得到系统类加载器  
				//System.out.println(url);
				URLClassLoader urlClassLoader= (URLClassLoader) ClassLoader.getSystemClassLoader();  
				//因为URLClassLoader中的addURL方法的权限为protected所以只能采用反射的方法调用addURL方法  
				Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });                                 
				add.setAccessible(true);  
				add.invoke(urlClassLoader, new Object[] {url });  
			}
		}
		
	}
	public static void main(String[] args) throws ClassNotFoundException {
		try {
			LoaderClassTools.loadJarsInDir("E:\\eclipse-workspace\\HTTP-netty-catserver\\target\\classes\\/webapps\\SeachBus\\WEB-INF\\\\lib");
			Class<?> c=Class.forName("oracle.jdbc.OracleDriver");   
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
