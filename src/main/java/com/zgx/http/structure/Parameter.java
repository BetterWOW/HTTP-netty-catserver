package com.zgx.http.structure;

import java.util.HashMap;

public class Parameter extends HashMap<String, String>{
	public String getParameter(String key){
		return this.get(key);
	}
	public void putParameter(String key,String value){
		this.put(key,value);
	}
}
