package com.zgx.http.session;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.zgx.tools.DateTimeTools;

public class SessionClearerThread implements Runnable {

	public void run() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(1000);
			long currentTime = DateTimeTools.currentTimeLong();
			Map<String, HttpSession> sessionMap = SessionManager.getSessionMap();
			for (Map.Entry<String, HttpSession> entry : sessionMap.entrySet()) {
				HttpSession session = entry.getValue();
				if((currentTime-session.getLastAccessedTime())>(session.getMaxInactiveInterval()*1000)) {
					synchronized (session) {
						session.invalidate();
					}
				}
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	//测试session
	public static void main(String[] args) {
		HttpSession session = SessionManager.createHttpSession(null);
		System.out.println(session.getId());
		System.out.println(SessionManager.getHttpSessionByID(session.getId()));
		Thread t = new Thread(new SessionClearerThread());
		t.start();
		try {
			
			Thread.sleep(1000*2);
			System.out.println(SessionManager.getHttpSessionByID(session.getId()));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
