package com.zgx.http.session;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import com.zgx.tools.DateTimeTools;

public class SessionManager {
	private static long sessionTime_UUID = 0;
	private static Map<String, String> idAndLastAccessTimeMap = new HashMap<String, String>();
	private static Map<String, HttpSession> sessionMap = new HashMap<String, HttpSession>();

	public static HttpSession createHttpSession(ServletContext servletContext) {
		HttpSession session = new SimpleSession(sessionTime_UUID++, servletContext);
		sessionMap.put(session.getId(), session);
		idAndLastAccessTimeMap.put(session.getId(), DateTimeTools.currentTimeLong()+"");;
//		System.out.println("SessionManager.createHttpSession"+idAndLastAccessTimeMap.get(session.getId()));
		return session;
	}

	public static long getLastAccessTimeMap(String sessionId) {
		return Long.parseLong(idAndLastAccessTimeMap.get(sessionId));
	}

	public static HttpSession getHttpSessionByID(String sessionID) {
		if (sessionMap.get(sessionID) != null) {
			idAndLastAccessTimeMap.put(sessionID, DateTimeTools.currentTimeLong() + "");
			return sessionMap.get(sessionID);
		} else {
			return null;
		}
	}
	
	public static void removeSession(HttpSession session) {
		
		idAndLastAccessTimeMap.remove(session.getId());
		sessionMap.remove(session.getId());
		
	}
	//本包可使用
	static Map<String, HttpSession> getSessionMap(){
		return sessionMap;
		
	}
	
	public static void main(String[] args) {
		System.out.println();
	}
}
