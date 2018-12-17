package com.zgx.tools;

public class StringTools {
	public static String trimSeriesBlankSpace(String str){
		if(str!=null)
			return str.replaceAll(" +", " ");
		else
			return null;
	}
	public static void main(String[] args) {
		System.out.println(("sdasd     dasd"));
		System.out.println(trimSeriesBlankSpace("     sdasd     dasd"));
	}
}
