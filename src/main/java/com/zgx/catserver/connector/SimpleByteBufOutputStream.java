package com.zgx.catserver.connector;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

public class SimpleByteBufOutputStream extends ServletOutputStream {
	private ByteBuf buf = null;

	public SimpleByteBufOutputStream(ByteBuf buf) {
		this.buf = buf;
	}

	public boolean isReady() {
		// TODO Auto-generated method stub
		return buf.writableBytes() > 0;
	}

	@Override
	public void setWriteListener(WriteListener writeListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(int b) throws IOException {
		// TODO Auto-generated method stub;
		this.buf.writeByte(b);

	}
}
