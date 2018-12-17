package com.zgx.catserver.connector;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

import com.zgx.http.HTTPResponseData;
import com.zgx.http.HTTPResponseData.ResponseHead;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

public class SimpleOutputStream extends ServletOutputStream {
	private ChannelHandlerContext ctx = null;
	private ResponseHeadByteBuf respHeadBuf = null;
	private HTTPResponseData httpResponseData = null;
	private ByteBuf respBodyBuf = Unpooled.buffer(8192, 8192 * 2);

	public SimpleOutputStream(ChannelHandlerContext ctx, ResponseHeadByteBuf respHeadBuf,
			HTTPResponseData httpResponseData) {
		this.ctx = ctx;
		this.respHeadBuf = respHeadBuf;
		this.httpResponseData = httpResponseData;
	}

	public boolean isReady() {
		// TODO Auto-generated method stub
		System.out.println("ready");
		if (!(respBodyBuf.isWritable())) {
			if (!respHeadBuf.isSend()) {
				if (this.httpResponseData.getResponseHead().getHead(ResponseHead.CONTENT_LENGTH) == null) {
					//this.httpResponseData.getResponseHead().putHead(ResponseHead.TRANSFER_ENCODING, "chunked");
				}
				byte[] headBytes = this.httpResponseData.headToBytes();
				this.respHeadBuf.getBuf().clear();
				this.respHeadBuf.getBuf().writeBytes(headBytes);
				ctx.channel().writeAndFlush(Unpooled.copiedBuffer(this.respHeadBuf.getBuf()));
				respHeadBuf.setSend(true);
			}
			ctx.channel().writeAndFlush(Unpooled.copiedBuffer(this.respBodyBuf));
			this.respBodyBuf.clear();
		}
		return respBodyBuf.writableBytes() > 0;
	}

	@Override
	public void setWriteListener(WriteListener writeListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(int b) throws IOException {
		// TODO Auto-generated method stub;
		if (!(respBodyBuf.isWritable())) {
			if (!respHeadBuf.isSend()) {
				if (this.httpResponseData.getResponseHead().getHead(ResponseHead.CONTENT_LENGTH) == null) {
					//this.httpResponseData.getResponseHead().putHead(ResponseHead.TRANSFER_ENCODING, "chunked");
					//System.out.println("SimpleOutputStream.write(int b)--"+httpResponseData.getResponseHead().getHead(ResponseHead.TRANSFER_ENCODING));
				}
				byte[] headBytes = this.httpResponseData.headToBytes();
				this.respHeadBuf.getBuf().clear();
				this.respHeadBuf.getBuf().writeBytes(headBytes);
				ctx.channel().writeAndFlush(Unpooled.copiedBuffer(this.respHeadBuf.getBuf()));
				respHeadBuf.setSend(true);
			}
			//System.out.println("SimpleOutputStream.write(int b)--ctx"+sendSize);
			ctx.channel().writeAndFlush(Unpooled.copiedBuffer(this.respBodyBuf));
			this.respBodyBuf.clear();
		}
		this.respBodyBuf.writeByte(b);

	}

	public ByteBuf getRespBodyBuf() {

		return this.respBodyBuf;
	}

}
