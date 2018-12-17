package com.zgx.catserver.connector;

import com.sun.net.httpserver.HttpContext;
import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class CatServerBootstrap {
	private final int port;

	public CatServerBootstrap(int port) {
		// TODO Auto-generated constructor stub
		this.port = port;
	}

	public void start() throws Exception {
		final CatserverRequestHandler catserverHandler = new CatserverRequestHandler();
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap bootstrap = new ServerBootstrap();
		// 指定线程组
		// 指定socket类型为NIO
		// 指定端口号并绑定InetSocketAddress到bootstrap的localAddress中
		// 添加Handler到ChannelPipe
		bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.localAddress(new InetSocketAddress(port)).childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						// TODO Auto-generated method stub
						ch.pipeline().addLast(new HTTPHeadDecoderHandler());
						ch.pipeline().addLast(catserverHandler);
					}
				});
		ChannelFuture f = bootstrap.bind(port).sync();
		// f.channel().closeFuture().sync();
	}

	public int getPort() {
		return port;
	}

	// 测试netty运行
	public static void main(String[] args) {
		try {
			new CatServerBootstrap(8080).start();
			System.out.println("PPPPPPPP");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
