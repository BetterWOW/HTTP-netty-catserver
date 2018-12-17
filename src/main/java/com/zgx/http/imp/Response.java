package com.zgx.http.imp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zgx.catserver.connector.ResponseHeadByteBuf;
import com.zgx.catserver.connector.SimpleByteBufOutputStream;
import com.zgx.catserver.connector.SimpleOutputStream;
import com.zgx.catserver.connector.SimplePrintWriter;
import com.zgx.http.HTTPResponseData;
import com.zgx.http.HTTPResponseData.ResponseHead;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

public class Response implements HttpServletResponse {
	/**
	 * respHeadBuf：响应头部的buf，ResponseHeadByteBuf不是真正的buf，而是ResponseHeadByteBuf中封装了buf
	 * sbbos：响应头部的输出流，实际输出是输出到ResponseHeadByteBuf中的buf headOut:响应头部的PrintWriter
	 * headOut:响应头部的printWriter
	 */
	private ResponseHeadByteBuf respHeadBuf = new ResponseHeadByteBuf(Unpooled.buffer(8192, 8192));
	private SimpleByteBufOutputStream sbbos = new SimpleByteBufOutputStream(respHeadBuf.getBuf());
	private PrintWriter headOut = new SimplePrintWriter(sbbos);
	/**
	 * outputStream：servlet的ServletOutputStream out：Servlet的PrintWriter
	 * bodyBuffer:消息体的Buffer
	 */
	private ServletOutputStream outputStream = null;
	private PrintWriter out = null;
	private ByteBuf bodyBuf = null;

	private HttpServletRequest request = null;
	private ChannelHandlerContext ctx = null;
	private HTTPResponseData httpResponseData = null;
	private String characterEncoding = Charset.defaultCharset().name();
	private Locale loc = Locale.getDefault();

	public Response(HTTPResponseData httpResponseData, ChannelHandlerContext ctx, HttpServletRequest request) {
		// TODO Auto-generated constructor stub
		this.ctx = ctx;
		this.request = request;
		this.httpResponseData = httpResponseData;
		this.outputStream = new SimpleOutputStream(ctx, this.respHeadBuf, this.httpResponseData);
		this.out = new SimplePrintWriter(this.outputStream);
		this.bodyBuf = ((SimpleOutputStream) this.outputStream).getRespBodyBuf();
		this.httpResponseData.getResponseHead().putHead(ResponseHead.CONTENT_LANGUAGE, loc.toString());

	}

	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return this.characterEncoding;
	}

	public String getContentType() {
		// TODO Auto-generated method stub
		return httpResponseData.getResponseHead().get(HTTPResponseData.ResponseHead.CONTENT_TYPE);
	}

	public ServletOutputStream getOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return this.outputStream;
	}

	public PrintWriter getWriter() throws IOException {
		// TODO Auto-generated method stub
		return this.out;
	}

	public void setCharacterEncoding(String charset) {
		this.characterEncoding = charset;
		this.httpResponseData.getResponseHead().putHead(HTTPResponseData.ResponseHead.CONTENT_ENCODING, charset);
		/**
		 * 待实现
		 */
	}

	public void setContentLength(int len) {
		// TODO Auto-generated method stub
		if (isCommitted()) {
			return;
		}
		this.httpResponseData.getResponseHead().putHead(HTTPResponseData.ResponseHead.CONTENT_LENGTH, "" + len);

	}

	public void setContentLengthLong(long len) {
		// TODO Auto-generated method stub
		this.httpResponseData.getResponseHead().putHead(HTTPResponseData.ResponseHead.CONTENT_LENGTH, "" + len);

	}

	public void setContentType(String type) {
		// TODO Auto-generated method stub
		this.httpResponseData.getResponseHead().putHead(HTTPResponseData.ResponseHead.CONTENT_TYPE, type);

	}

	public void setBufferSize(int size) {
		// TODO Auto-generated method stub
		ByteBuf bodyBuf = ((SimpleOutputStream) this.outputStream).getRespBodyBuf();
		bodyBuf.capacity(size);

	}

	public int getBufferSize() {
		// TODO Auto-generated method stub
		return this.bodyBuf.capacity();
	}

	public void flushBuffer() throws IOException {
		//System.out.println("Respons.flushBuffer():===123");

		// TODO Auto-generated method stub
		if (this.httpResponseData.getResponseHead().getHead(ResponseHead.CONTENT_LENGTH) == null
				&& this.httpResponseData.getResponseHead().getHead(ResponseHead.TRANSFER_ENCODING) == null) {
			this.httpResponseData.getResponseHead().put(ResponseHead.CONTENT_LENGTH, this.bodyBuf.readableBytes() + "");
		}
		if (!this.respHeadBuf.isSend()) {
			this.respHeadBuf.getBuf().clear();
			this.respHeadBuf.getBuf().writeBytes(this.httpResponseData.headToBytes());
			this.ctx.channel().writeAndFlush(Unpooled.copiedBuffer(respHeadBuf.getBuf()));
			this.respHeadBuf.setSend(true);
		}
		this.ctx.channel().writeAndFlush(Unpooled.copiedBuffer(this.bodyBuf));
		byte[] readStr = new byte[bodyBuf.readableBytes()];
		bodyBuf.readBytes(readStr);
		String str = new String(readStr);
//		System.out.println("Response.flushBuffer"+this.httpResponseData.getResponseHead().getHead(HTTPResponseData.ResponseHead.CONTENT_LENGTH));
//		System.out.println("Response.flushBuffer"+str);
		this.bodyBuf.clear();
		

	}

	public void resetBuffer() {
		// TODO Auto-generated method stub
		this.bodyBuf.resetReaderIndex();
		this.bodyBuf.resetWriterIndex();

	}

	public boolean isCommitted() {
		// TODO Auto-generated method stub
		return this.respHeadBuf.isSend();
	}

	public void reset() {
		// TODO Auto-generated method stub
		this.respHeadBuf.getBuf().clear();
		this.bodyBuf.clear();
		this.httpResponseData.getResponseHead().clearHead();
		this.setContentType(HTTPResponseData.ContentType.TEXT_HTML);
		this.httpResponseData.getResponseHead().putHead(ResponseHead.CONTENT_LANGUAGE, loc.toString());

	}

	public void setLocale(Locale loc) {
		// TODO Auto-generated method stub
		this.httpResponseData.getResponseHead().putHead(ResponseHead.CONTENT_LANGUAGE, loc.toString());
		this.loc = loc;

	}

	public Locale getLocale() {
		// TODO Auto-generated method stub
		return this.loc;
	}

	public void addCookie(Cookie cookie) {
		// TODO Auto-generated method stub
		this.httpResponseData.addCookie(cookie);
	}

	public boolean containsHeader(String name) {
		// TODO Auto-generated method stub
		return this.httpResponseData.getResponseHead().getHead(name) != null;
	}

	public String encodeURL(String url) {
		// TODO Auto-generated method stub
		return null;
		/**
		 * 待实现
		 */
	}

	public String encodeRedirectURL(String url) {
		// TODO Auto-generated method stub
		return null;
		/**
		 * 待实现
		 */
	}

	public String encodeUrl(String url) {
		// TODO Auto-generated method stub
		return null;
		/**
		 * 待实现
		 */
	}

	public String encodeRedirectUrl(String url) {
		// TODO Auto-generated method stub
		return null;
		/**
		 * 待实现
		 */
	}

	public void sendError(int sc, String msg) throws IOException {
		// TODO Auto-generated method stub
		// 将状态修改并刷新响应头部缓冲区
		this.httpResponseData.getStatusLine().setStatusCode(sc);
		this.httpResponseData.getResponseHead().putHead(ResponseHead.CONTENT_LENGTH, msg.length() + "");
		this.respHeadBuf.getBuf().clear();
		this.respHeadBuf.getBuf().writeBytes(this.httpResponseData.headToBytes());
		// 将bodybuf清空，将内容写入bobybuf
		this.bodyBuf.clear();
		this.out.print(msg);
		// 将响应返回
		this.ctx.channel().writeAndFlush(Unpooled.copiedBuffer(respHeadBuf.getBuf()));
		this.respHeadBuf.setSend(true);
		this.ctx.channel().writeAndFlush(Unpooled.copiedBuffer(this.bodyBuf));
		this.bodyBuf.clear();
		// 将缓冲区置为只读
		this.bodyBuf = this.bodyBuf.asReadOnly();

	}

	public void sendError(int sc) throws IOException {
		// TODO Auto-generated method stub
		this.sendError(sc, "");
	}

	public void sendRedirect(String location) throws IOException {
		// TODO Auto-generated method stub
		this.httpResponseData.getStatusLine().setStatusCode(302);
		this.httpResponseData.getResponseHead().put(HTTPResponseData.ResponseHead.LOCATION, location);
		

	}

	public void setDateHeader(String name, long date) {
		// TODO Auto-generated method stub
		this.httpResponseData.getResponseHead().putHead(name, date + "");
		System.out.println("putdate");

	}

	public void addDateHeader(String name, long date) {
		// TODO Auto-generated method stub
		/**
		 * 待实现
		 */

	}

	public void setHeader(String name, String value) {
		// TODO Auto-generated method stub
		this.httpResponseData.getResponseHead().putHead(name, value);

	}

	public void addHeader(String name, String value) {
		// TODO Auto-generated method stub
		/**
		 * 待实现
		 */

	}

	public void setIntHeader(String name, int value) {
		// TODO Auto-generated method stub
		this.httpResponseData.getResponseHead().putHead(name, value + "");

	}

	public void addIntHeader(String name, int value) {
		// TODO Auto-generated method stub
		/**
		 * 待实现
		 */
	}

	public void setStatus(int sc) {
		// TODO Auto-generated method stub
		this.httpResponseData.getStatusLine().setStatusCode(sc);

	}

	public void setStatus(int sc, String sm) {
		// TODO Auto-generated method stub
		this.httpResponseData.getStatusLine().setStatusCode(sc);
		this.httpResponseData.getStatusLine().setStatus(sm);

	}

	public int getStatus() {
		// TODO Auto-generated method stub
		return this.httpResponseData.getStatusLine().getStatusCode();
	}

	public String getHeader(String name) {
		// TODO Auto-generated method stub
		return this.httpResponseData.getResponseHead().getHead(name);
	}

	public Collection<String> getHeaders(String name) {
		// TODO Auto-generated method stub
		/**
		 * 待实现
		 */
		return null;
	}

	public Collection<String> getHeaderNames() {
		// TODO Auto-generated method stub
		/**
		 * 待实现
		 */
		return null;
	}

}
