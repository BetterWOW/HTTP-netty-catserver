package com.zgx.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;

import com.zgx.catserver.connector.SimpleByteBufOutputStream;
import com.zgx.catserver.connector.SimplePrintWriter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class HTTPResponseData {
	private StatusLine statusLine = new StatusLine();
	private ResponseHead responseHead = new ResponseHead();
	private String URI;

	public void addCookie(Cookie cookie) {
		String headCookies = this.getResponseHead().getHead(HTTPResponseData.ResponseHead.SET_COOKIE);

		// 得到传入cookie的内容
		String headValue = cookie.getName() + "=" + cookie.getValue();
		if (cookie.getComment() != null) {
			headValue += "; comment" + "=" + cookie.getComment();
		}
		// httponly
		if (cookie.isHttpOnly()) {
			headValue += "; HttpOnly";
		}
		// secure
		if (cookie.getSecure()) {
			headValue += "; Secure";
		}
		if (cookie.getDomain() != null) {
			headValue += ("; Pomain" + "=" + cookie.getDomain());
		}
		if (cookie.getMaxAge() > 0) {
			headValue += ("; Max-Age " + "=" + cookie.getMaxAge());
		}
		if (cookie.getPath() != null) {
			headValue += ("; Path" + "=" + cookie.getPath());
		}
		headValue += ("; Version" + "=" + cookie.getVersion());

		// 检查cookie头部是否已经存在，存在则追加，否者直接加入
		if (headCookies != null) {
			String headKey = "Set-Cookie: ";
			headCookies += "\r\n" + headKey + headValue;
		} else {
			headCookies = headValue;
		}

		this.getResponseHead().putHead(HTTPResponseData.ResponseHead.SET_COOKIE, headCookies);

		// byte[] bytes = new byte[this.respHeadBuf.getBuf().readableBytes()];
		// this.respHeadBuf.getBuf().readBytes(bytes);
		// System.out.println(new String(bytes));

	}

	public byte[] headToBytes() {
		ByteBuf buf = Unpooled.buffer();
		SimpleByteBufOutputStream sbbos = new SimpleByteBufOutputStream(buf);
		SimplePrintWriter out = new SimplePrintWriter(sbbos);
		out.print(this.statusLine.protocolVersion);
		out.print((" " + this.statusLine.getStatusCode()));
		out.println(" " + StatusCode.getStatusMessage(this.statusLine.getStatusCode()));
		for (Entry<String, String> entry : this.responseHead.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			out.println(key + ":" + value);
		}
		out.println();
		out.flush();
		byte[] result = new byte[buf.readableBytes()];
		buf.readBytes(result);
		return result;

	}

	public StatusLine getStatusLine() {
		return statusLine;
	}

	public void setStatusLine(StatusLine statusLine) {
		this.statusLine = statusLine;
	}

	public ResponseHead getResponseHead() {
		return responseHead;
	}

	public void setResponseHead(ResponseHead responseHead) {
		this.responseHead = responseHead;
	}

	public String getURI() {
		return URI;
	}

	public void setURI(String uRI) {
		URI = uRI;
	}

	public static class StatusLine {
		private String protocolVersion = "HTTP/1.1";
		private int statusCode;
		private String status;

		public String getProtocolVersion() {
			return protocolVersion;
		}

		public void setProtocolVersion(String protocolVersion) {
			this.protocolVersion = protocolVersion;
		}

		public int getStatusCode() {
			return statusCode;
		}

		public void setStatusCode(int statusCode) {
			this.setStatus(StatusCode.getStatusMessage(statusCode));
			this.statusCode = statusCode;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

	}

	static public class ResponseHead extends HashMap<String, String> {
		public static final String ALLOW = "Allow";
		public static final String ACCEPT_RANGES = "Accept-Ranges";
		public static final String AGE = "Age";
		public static final String CACHE_CONTROL = "Cache-Control";
		public static final String CONTENT_ENCODING = "Content-Encoding";
		public static final String CONTENT_LANGUAGE = "Content-Language";
		public static final String CONTENT_LENGTH = "Content-Length";
		public static final String CONTENT_LOCATION = "Content-Location";
		public static final String CONTENT_MD5 = "Content-MD5";
		public static final String CONTENT_RANGE = "Content-Range";
		public static final String CONTENT_TYPE = "Content-Type";
		public static final String DATE = "Date";
		public static final String ETAG = "ETag";
		public static final String EXPIRES = "Expires";
		public static final String LAST_MODIFIED = "Last-Modified";
		public static final String LOCATION = "Location";
		public static final String PRAGMA = "Pragma";
		public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";
		public static final String REFRESH = "refresh";
		public static final String RETRY_AFTER = "Retry-After";
		public static final String SERVER = "Server";
		public static final String SET_COOKIE = "Set-Cookie";
		public static final String TRAILER = "Trailer";
		public static final String TRANSFER_ENCODING = "Transfer-Encoding";
		public static final String VARY = "Vary";
		public static final String VIA = "Via";
		public static final String WARNING = "Warning";
		public static final String WWW_AUTHENTICATE = "WWW-Authenticate";

		public void clearHead() {
			this.clear();
		}

		public void putHead(String key, String value) {
			this.put(key, value);
		}

		public String getHead(String key) {
			return this.get(key);

		}
	}

	public static class StatusCode {
		public static final int CONTINUE = 100;
		public static final int SWITCHING_PROTOCOLS = 101;
		public static final int OK = 200;
		public static final int CREATED = 201;
		public static final int ACCEPTED = 202;
		public static final int NON_AUTHORITATIVE_INFORMATION = 203;
		public static final int NO_CONTENT = 204;
		public static final int RESET_CONTENT = 205;
		public static final int PARTIAL_CONTENT = 206;
		public static final int MULTIPLE_CHOICES = 300;
		public static final int MOVED_PERMANENTLY = 301;
		public static final int FOUND = 302;
		public static final int SEE_OTHER = 303;
		public static final int NOT_MODIFIED = 304;
		public static final int USE_PROXY = 305;
		public static final int UNUSED = 306;
		public static final int TEMPORARY_REDIRECT = 307;
		public static final int BAD_REQUEST = 400;
		public static final int UNAUTHORIZED = 401;
		public static final int PAYMENT_REQUIRED = 402;
		public static final int FORBIDDEN = 403;
		public static final int NOT_FOUND = 404;
		public static final int METHOD_NOT_ALLOWED = 405;
		public static final int NOT_ACCEPTABLE = 406;
		public static final int PROXY_AUTHENTICATION_REQUIRED = 407;
		public static final int REQUEST_TIME_OUT = 408;
		public static final int CONFLICT = 409;
		public static final int GONE = 410;
		public static final int LENGTH_REQUIRED = 411;
		public static final int PRECONDITION_FAILED = 412;
		public static final int REQUEST_ENTITY_TOO_LARGE = 413;
		public static final int REQUEST_URI_TOO_LARGE = 414;
		public static final int UNSUPPORTED_MEDIA_TYPE = 415;
		public static final int REQUESTED_RANGE_NOT_SATISFIABLE = 416;
		public static final int EXPECTATION_FAILED = 417;
		public static final int INTERNAL_SERVER_ERROR = 500;
		public static final int NOT_IMPLEMENTED = 501;
		public static final int BAD_GATEWAY = 502;
		public static final int SERVICE_UNAVAILABLE = 503;
		public static final int GATEWAY_TIME_OUT = 504;
		public static final int HTTP_VERSION_NOT_SUPPORTED = 505;

		public static String getStatusMessage(int codeNum) {
			switch (codeNum) {
			case CONTINUE:
				return "Continue";
			case SWITCHING_PROTOCOLS:
				return "Switching Protocols";
			case OK:
				return "OK";
			case CREATED:
				return "Created";
			case ACCEPTED:
				return "Accepted";
			case NON_AUTHORITATIVE_INFORMATION:
				return "Non-Authoritative Information";
			case NO_CONTENT:
				return "No Content";
			case RESET_CONTENT:
				return "Reset Content";
			case PARTIAL_CONTENT:
				return "Partial Content";
			case MULTIPLE_CHOICES:
				return "Multiple Choices";
			case MOVED_PERMANENTLY:
				return "Moved Permanently";
			case FOUND:
				return "Found";
			case SEE_OTHER:
				return "See Other";
			case NOT_MODIFIED:
				return "Not_Modified";
			case USE_PROXY:
				return "Use Proxy";
			case UNUSED:
				return "Unused";
			case TEMPORARY_REDIRECT:
				return "Temporary Redirect";
			case BAD_REQUEST:
				return "Bad Request";
			case UNAUTHORIZED:
				return "Unauthorized";
			case PAYMENT_REQUIRED:
				return "Payment Required";
			case FORBIDDEN:
				return "Forbidden";
			case NOT_FOUND:
				return "Not Found";
			case METHOD_NOT_ALLOWED:
				return "Method Not Allowed";
			case NOT_ACCEPTABLE:
				return "Not Acceptable";
			case PROXY_AUTHENTICATION_REQUIRED:
				return "Proxy Authentication Required";
			case REQUEST_TIME_OUT:
				return "Request Time-out";
			case CONFLICT:
				return "Conflict";
			case GONE:
				return "Gone";
			case LENGTH_REQUIRED:
				return "Length Required";
			case PRECONDITION_FAILED:
				return "Precondition Failed";
			case REQUEST_ENTITY_TOO_LARGE:
				return "Request Entity Too Large";
			case REQUEST_URI_TOO_LARGE:
				return "Request-URI Too Large";
			case UNSUPPORTED_MEDIA_TYPE:
				return "Unsupported Media Type";
			case REQUESTED_RANGE_NOT_SATISFIABLE:
				return "Requested range not satisfiable	";
			case EXPECTATION_FAILED:
				return "Expectation Failed";
			case INTERNAL_SERVER_ERROR:
				return "Internal Server Error";
			case NOT_IMPLEMENTED:
				return "Not Implemented";
			case BAD_GATEWAY:
				return "Bad Gateway";
			case SERVICE_UNAVAILABLE:
				return "Service Unavailable";
			case GATEWAY_TIME_OUT:
				return "Gateway Time-out";
			case HTTP_VERSION_NOT_SUPPORTED:
				return "HTTP Version not supported";
			default:
				break;
			}
			return null;
		}

	}

	public static class ContentType {
		public static final String TEXT_HTML = "text/html";
		public static final String TEXT_PLAIN = "text/plain";
		public static final String TEXT_XML = "text/xml";
		public static final String TEXT_ASP = "text/asp";
		public static final String TEXT_CSS = "text/css";
		public static final String IMAGE_GIF = "image/gif";
		public static final String IMAGE_PNG = " image/png";
		public static final String IMAGE_JPEG = "image/jpeg";
		public static final String VIDEO_MPEG4 = "video/mpeg4";

		public static final String APPLICATION_XHTML_XML = "application/xhtml+xml";
		public static final String APPLICATION_XML = "application/xml";
		public static final String APPLICATION_ZIP = "application/zip";
		public static final String APPLICATION_JS = "application/javascript";
		public static final String APPLICATION_ATOM_XML = "application/atom+xml";
		public static final String APPLICATION_JSON = "application/json";
		public static final String APPLICATION_PDF = "application/pdf";
		public static final String APPLICATION_MSWORD = "application/msword";
		public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
		public static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";

		public static final String MULTIPART_FORM_DATA = "multipart/form-data ";

	}
}
