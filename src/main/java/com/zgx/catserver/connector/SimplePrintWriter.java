package com.zgx.catserver.connector;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import com.zgx.http.HTTPResponseData;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

public class SimplePrintWriter extends PrintWriter {

	@Override
	public void write(int c) {
		// TODO Auto-generated method stub
		super.write(c+"");
	}
	@Override
	public void write(String s) {
		// TODO Auto-generated method stub
		s = ""+s;
		super.write(s);
	}
	@Override
	public void write(String s, int off, int len) {
		// TODO Auto-generated method stub
		//System.out.println(s);
		super.write(""+s, off, len);
		this.flush();
	}
	@Override
	public void write(char[] buf, int off, int len) {
		// TODO Auto-generated method stub
		super.write(buf, off, len);
		this.flush();
	}
	public SimplePrintWriter(OutputStream out) {
		super(out,true);
		
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void print(String s) {
		// TODO Auto-generated method stub
		super.print(""+s);
		this.flush();
	}
	
	//测试outputstream和printwriter
	public static void main(String[] args) throws IOException {
		ResponseHeadByteBuf respHeadBuf = new ResponseHeadByteBuf(Unpooled.buffer(8192, 8192));
		ByteBuf respBodyBuf = Unpooled.buffer(8192, 8192 * 2);
		ChannelHandlerContext ctx = null;
		//HTTPResponseData httpResponseData;
		SimpleOutputStream simpleOutputStream = new SimpleOutputStream(ctx,respHeadBuf,null);
		
		SimplePrintWriter writer = new SimplePrintWriter(simpleOutputStream);
		System.out.println("writer start");
		//simpleOutputStream.write("ouputstream-ok".getBytes());
		//writer.println("succeed");
		writer.write("张");
		respBodyBuf.resetReaderIndex();
		byte[] bytes = new byte[respBodyBuf.readableBytes()];
		System.out.println(new String(bytes));
		//writer.flush();
		byte[] dts = new byte[respBodyBuf.readableBytes()];
		respBodyBuf.readBytes(dts);
		System.out.println(new String(dts));
		//writer.println("succeed");

	}
}
