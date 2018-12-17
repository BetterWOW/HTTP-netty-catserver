package com.zgx.catserver.connector;

import java.io.Console;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.List;

import com.zgx.http.HTTPRequestData;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;
/**
 * 解析请求的解码器
 * @author userzgx
 *
 */
public class HTTPHeadDecoderHandler extends ByteToMessageDecoder {
	private boolean hasDecodeLineAndHeader = false;
	private HTTPRequestData httpRequestData = null;
	private int bodyBeginIndex = -1;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// TODO Auto-generated method stub
		if (!hasDecodeLineAndHeader) {
			byte[] recBytes = new byte[in.readableBytes()];
			in.readBytes(recBytes);
			String recData = new String(recBytes, "ASCII");
			// recData = new String(recData.getBytes("ISO-8859-1"),"utf8");
			String checkHeader = "\r\n\r\n";
//			System.out.println("receive");
//			System.out.println(recData);
//			System.out.println("decoder");
			// out.add(recData);
			if (!recData.contains(checkHeader)) {
				in.resetReaderIndex();
				return;
			}
			int requestLineAndHeadLen = recData.indexOf(checkHeader);
			String requestLineAndHeader = recData.substring(0, requestLineAndHeadLen);
//			System.out.println(requestLineAndHeadLen);
//			System.out.println("#########################################");
//			System.out.println(requestLineAndHeader);
//			System.out.println("#########################################");
			bodyBeginIndex = requestLineAndHeadLen + 4;
			in.readerIndex(bodyBeginIndex);
			in.discardReadBytes();
			this.httpRequestData = HTTPRequestData.getHTTPRequestDataInstance(requestLineAndHeader);
			hasDecodeLineAndHeader = true;
		}
		if (hasDecodeLineAndHeader&&httpRequestData.getRequestHead("content-length") != null) {
//			System.out.println(httpRequestData.getRequestHead("content-length"));
			int bodyLength = Integer.parseInt(httpRequestData.getRequestHead("content-length"));
			byte[] bodyBytes = new byte[in.readableBytes()];
			if (bodyLength == bodyBytes.length) {
				in.readBytes(bodyBytes);
				this.httpRequestData.setContent(new String(bodyBytes));
				in.clear();
				out.add(this.httpRequestData);
				this.hasDecodeLineAndHeader = false;
			}

		} else if(hasDecodeLineAndHeader){
			in.clear();
			out.add(this.httpRequestData);
			//System.out.println("HTTPHeadDecoderHandler.decode---");
			this.hasDecodeLineAndHeader = false;
		}

	}

}
