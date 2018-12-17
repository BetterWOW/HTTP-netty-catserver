package com.zgx.http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.zgx.http.structure.Parameter;

public class HTTPRequestData {
	public static String ACCEPT = "Accept";
	public static String ACCEPT_CHARSET = "Accept-Charset";
	public static String ACCEPT_ENCODING = "Accept-Encoding";
	public static String ACCEPT_LANGUAGE = "Accept-Language";
	public static String ACCEPT_DATETIME = "Accept-Datetime";
	public static String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
	public static String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
	public static String AUTHORIZATION = "Authorization";
	public static String CACHE_CONTROL = "Cache-Control";
	public static String CONNECTION = "Connection";
	public static String COOKIE = "Cookie";
	public static String CONTENT_LENGTH = "Content-Length";
	public static String CONTENT_MD5 = "Content-MD5";
	public static String CONTENT_TYPE = "Content-Type";
	public static String DATE = "Date";
	public static String EXPECT = "Expect";
	public static String FORWARDED = "Forwarded";
	public static String FROM = "From";
	public static String HOST = "Host";
	public static String IF_MATCH = "If-Match";
	public static String IF_MODIFIED_SINCE = "If-Modified-Since";
	public static String IF_NONE_MATCH = "If-None-Match";
	public static String IF_RANGE = "If-Range";
	public static String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
	public static String MAX_FORWARDS = "Max-Forwards";
	public static String ORIGIN = "Origin";
	public static String PRAGMA = "Pragma";
	public static String PROXY_AUTHORIZATION = "Proxy-Authorization";
	public static String RANGE = "Range";
	public static String REFERER = "Referer";
	public static String TE = "TE";
	public static String USER_AGENT = "User-Agent";
	public static String UPGRADE = "Upgrade";
	public static String VIA = "Via";
	public static String WARNING = "Warning";

	private RequestLine requestLine = new RequestLine();
	private RequestHead requestHead = new RequestHead();
	private String content = "";

	public static HTTPRequestData getHTTPRequestDataInstance(String requestLineAndHeader)
			throws ExecutionException, UnsupportedEncodingException {
		if (requestLineAndHeader == null || requestLineAndHeader.equals(""))
			throw new ExecutionException(new Throwable("参数错误"));
		String[] strs = requestLineAndHeader.split("\r\n");
		HTTPRequestData result = new HTTPRequestData();
		// 对请求行进行解析
		String[] rL = strs[0].split(" ");
		if (rL.length != 3)
			throw new ExecutionException(new Throwable("参数错误"));
		// 对请求头部进行解析并将结果放到RequestHead
		for (int i = 1; i < strs.length; i++) {
			String[] tmp = strs[i].split(":", 2);
			result.getRequestHead().put(tmp[0].toUpperCase().trim(), tmp[1].trim());
		}
		// 将请求行结果放到RequestHead
		result.getRequestLine().setMethod(rL[0].trim());
		result.getRequestLine().setURL(URLDecoder.decode(rL[1], Charset.defaultCharset().name()));
		result.getRequestLine().setProtocolVersion(rL[2].trim());

		return result;

	}

	private HTTPRequestData() {

	}

	public RequestLine getRequestLine() {
		return requestLine;
	}

	public RequestHead getRequestHead() {
		return requestHead;
	}

	public String getRequestHead(String key) {
		if (this.requestHead.getHead(key.toUpperCase()) != null)
			return this.requestHead.getHead(key.toUpperCase()).toUpperCase();
		else
			return null;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) throws Exception {
		System.out.println("HTTPResponseData.setContent(String content)" + content);
		if (this.getRequestHead(HTTPRequestData.CONTENT_TYPE) != null) {
			String contentType = this.getRequestHead(HTTPRequestData.CONTENT_TYPE).toUpperCase();
			// 这里暂时只解析“APPLICATION/X-WWW-FORM-URLENCODED”类型的内容
			if (contentType.equals("APPLICATION/X-WWW-FORM-URLENCODED")) {
				if (content != null)
					this.content = new String(URLDecoder.decode(content, Charset.defaultCharset().name()));
				else
					throw new NullPointerException("参数null");

			}
			/**
			 * 其它请求数据类型
			 */
		}
	}

	static public class RequestHead extends HashMap<String, String> {

		@Override
		public String get(Object key) {
			// TODO Auto-generated method stub
			if (key != null)
				key = ((String) key).toUpperCase();
			return super.get(key);
		}

		@Override
		public boolean containsKey(Object key) {
			// TODO Auto-generated method stub
			if (key != null)
				key = ((String) key).toUpperCase();
			return super.containsKey(key);
		}

		@Override
		public String put(String key, String value) {
			// TODO Auto-generated method stub
			if (key != null)
				key = ((String) key).toUpperCase();
			return super.put(key, value);
		}

		@Override
		public String remove(Object key) {
			// TODO Auto-generated method stub
			if (key != null)
				key = ((String) key).toUpperCase();
			return super.remove(key);
		}

		public void putHead(String key, String value) {
			this.put(key, value);
		}

		public String getHead(String key) {
			return this.get(key);

		}
	}

	/**
	 * 请求行类
	 * 
	 * @author userzgx
	 *
	 */
	static public class RequestLine {
		private String method;
		private String URL;
		private String protocolVersion;

		public String getMethod() {
			return method;
		}

		public void setMethod(String method) {
			this.method = method.trim().toUpperCase();
		}

		public String getURL() {
			return URL;
		}

		public void setURL(String URL) {
			this.URL = URL.trim();
		}

		public String getProtocolVersion() {
			return protocolVersion;
		}

		public void setProtocolVersion(String protocolVersion) {
			this.protocolVersion = protocolVersion.trim().toUpperCase();
		}

	}
}
