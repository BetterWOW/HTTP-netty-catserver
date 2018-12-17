package com.zgx.http.session;

import javax.servlet.http.Cookie;

public class SimpleCookie extends Cookie{

	public SimpleCookie(String name, String value) {
		super(name, value);
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) {
		Cookie c=new SimpleCookie("a", "a");
		c.setMaxAge(235);
		System.out.println(c.getVersion());
		System.out.println("fasdf".split(";")[0]);
	}

}
