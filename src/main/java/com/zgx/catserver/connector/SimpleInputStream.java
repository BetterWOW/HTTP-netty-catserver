package com.zgx.catserver.connector;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

import io.netty.buffer.ByteBuf;

public class SimpleInputStream extends ServletInputStream {
	private ByteBuf buf = null;

	public SimpleInputStream(ByteBuf buf) {
		// TODO Auto-generated constructor stub
		this.buf = buf;
	}

	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return !buf.isReadable();
	}

	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return buf.isReadable();
	}

	@Override
	public void setReadListener(ReadListener readListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public int read() throws IOException {
		// TODO Auto-generated method stub
		if(this.buf.isReadable()) {
			return this.buf.readByte();
		} else {
			return -1;
		}
			
	}

}
