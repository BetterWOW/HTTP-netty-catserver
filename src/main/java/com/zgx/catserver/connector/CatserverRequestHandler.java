package com.zgx.catserver.connector;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

import com.zgx.http.HTTPRequestData;
import com.zgx.http.HTTPResponseData;
import com.zgx.http.imp.Request;
import com.zgx.http.imp.Response;
import com.zgx.http.servlet.engine.ServletContextManager;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.CharsetUtil;

/**
 * 
 * @author userzgx
 *
 */
@Sharable
public class CatserverRequestHandler extends ChannelInboundHandlerAdapter {
	private int count = 0;
	@Override
	public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		// ByteBuf in = (ByteBuf)msg;
		// String receive = in.toString(CharsetUtil.US_ASCII);
		HTTPRequestData httpRequestData = (HTTPRequestData) msg;
		// 创建HTTPResponseData
		HTTPResponseData httpResponseData = new HTTPResponseData();

		// 根据请求头的URL构造ServletContext的Uri和Servlet的Uri
		String reqURL = httpRequestData.getRequestLine().getURL();
		String contextUri = "/";
		if (reqURL.split("/").length > 1)
			contextUri = "/" + reqURL.split("/")[1];
		String servletUri = reqURL;
		if (reqURL.contains("?")) {
			servletUri = reqURL.substring(0, reqURL.indexOf("?"));
		}
		// //获取一个Uri为contextUri的ServletContext sc
		ServletContext sc = ServletContextManager.getContext(contextUri);

		Servlet servlet = null;
		if (sc != null) {
			// 匹配对应资源的servletName
			String servletName = ServletContextManager.getServletName(servletUri);
			// System.out.println(sc);//测试sc是否与jsp的sc一致
			// 如果没有匹配的servlet，使用defaultHttpServlet执行
			if (servletName == null) {
				servlet = sc.getServlet("defaultHttpServlet");
				// System.out.println(" CatserverRequestHandler --------73:defaultHttpServlet" +
				// servlet);

			} else {
				// 获取一个servletName为servletName的servlet
				servlet = sc.getServlet(servletName);
			}
		}
		// 生成HttpRequest和HttpResponse
		HttpServletRequest request = new Request(httpRequestData, ctx, httpResponseData);
		HttpServletResponse response = new Response(httpResponseData, ctx, request);
		//System.out.println(response.getHeader(HTTPResponseData.ResponseHead.TRANSFER_ENCODING));
		if (servlet != null) {
			response.setContentType(HTTPResponseData.ContentType.TEXT_HTML);
			response.setStatus(200);
			servlet.service(request, response);
			response.flushBuffer();
			// 检查TRANSFER_ENCODING是否为chunked
			//System.out.println(response.getHeader(HTTPResponseData.ResponseHead.TRANSFER_ENCODING));
			String chunked = response.getHeader(HTTPResponseData.ResponseHead.TRANSFER_ENCODING);
			if (chunked != null && chunked.equals("chunked")) {
				String chunkedEnd = "\r\n0\r\n\r\n";
				ctx.writeAndFlush(Unpooled.buffer().writeBytes(chunkedEnd.getBytes()));
			}

		} else {
			response.sendError(HTTPResponseData.StatusCode.NOT_FOUND,
					"<p>\n" + "<font size=\"10\" face=\"Times\">\n" + "404 Not Found.\n" + "</font>\n" + "</p>");
			response.flushBuffer();
		}
		super.channelRead(ctx, msg);
		String connectionStat = httpRequestData.getRequestHead(HTTPRequestData.CONNECTION);
		// ctx.channel().close();
		// ctx.close();
		// 发送一个空的ByteBuf作为结束事件，为此次的发送添加监听器，完成后关闭流
		ChannelProgressivePromise progressivePromise = ctx.channel().newProgressivePromise();
		progressivePromise.addListener(new ChannelProgressiveFutureListener() {

			public void operationComplete(ChannelProgressiveFuture future) throws Exception {
				// TODO Auto-generated method stub
				//System.out.println("operationComplete");
				ctx.close();

			}
			public void operationProgressed(ChannelProgressiveFuture future, long progress, long total)
					throws Exception {
				// TODO Auto-generated method stub
				//System.out.println("operationProgressed");
			}
		});
		ctx.writeAndFlush(Unpooled.buffer(), progressivePromise);

		if (connectionStat == null || !connectionStat.toLowerCase().equals("keep-alive")) {
			ctx.close();
		}

	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		ctx.write(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
		super.channelReadComplete(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		cause.printStackTrace();
		ctx.close();
		super.exceptionCaught(ctx, cause);
	}

	private HTTPRequestData getHTTPRequestData(Object obj) {
		return null;

	}
	// 88行开始测试代码
	// System.out.println("CatserverRequestHandler.channelRead ---flush");
	// System.out.println(response.getStatus());
	//
	// ByteBuf result = Unpooled.copiedBuffer((("HTTP/1.1 200 ok\r\n" +
	// "CONNECTION: close\r\n" +
	// "Content-Type:text/html;charset:GBK\r\n" +
	// "\r\n" +
	// "dsadasd111").getBytes()));
	// ctx.write(result);
	// ctx.flush();

	// HTTP/1.1 200 ok
	// CONNECTION: close
	// Content-Type:text/html;charset:GBK
	//
	// dsadasd111
	// 测试多线程下ctx是否可以正常使用，测试暂时结果为正常
	// new Thread(new Runnable() {
	//
	// public void run() {
	// // TODO Auto-generated method stub
	// ByteBuf result = Unpooled.copiedBuffer((("HTTP/1.1 200 ok\r\n" +
	// "CONNECTION: close\r\n" +
	// "Content-Type:text/html;charset:GBK\r\n" +
	// "\r\n" +
	// "dsadasd111").getBytes()));
	// ctx.write(result);
	// ctx.flush();
	// try {
	// Thread.sleep(1000*30);
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// ctx.write(Unpooled.copiedBuffer("\r\nOK".getBytes()));
	// ctx.flush();
	// System.out.println("out");
	//
	// }
	// }).start();

	// ServletRequestWrapper srw ;
	// ByteBuf result = Unpooled.copiedBuffer((("HTTP/1.1 200 ok\r\n" +
	// "CONNECTION: close\r\n" +
	// "Content-Type:text/html;charset:GBK\r\n" +
	// "\r\n" +
	// "dsadasd111").getBytes()));
	// ctx.write(result);
	// ctx.flush();
	// Thread.sleep(1000*30);
	// ctx.write(Unpooled.copiedBuffer("\r\nOK".getBytes()));
	// ctx.flush();
	// System.out.println("out");
}
