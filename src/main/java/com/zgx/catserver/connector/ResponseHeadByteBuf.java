package com.zgx.catserver.connector;

import io.netty.buffer.ByteBuf;

public class ResponseHeadByteBuf {
	private ByteBuf buf = null;
	private Boolean send = false;
	public ResponseHeadByteBuf(ByteBuf buf) {
		// TODO Auto-generated constructor stub
		this.buf = buf;
	}
	public boolean isSend() {
		return send;
	}
	public void setSend(boolean send) {
		this.send = send;
	}
	public ByteBuf getBuf() {
		return buf;
	}
	
}
