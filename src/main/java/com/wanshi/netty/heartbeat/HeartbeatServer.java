package com.wanshi.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class HeartbeatServer {

    public static void main(String[] args) throws Exception{
        //创建2个线程
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //监听服务器
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            //设置一些前置参数，采用链式编程
            serverBootstrap.group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //获取通道
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //为通道设置一些处理器
                            //IdleStateHandler是Netty 提供的处理空闲状态的处理器
                            // long readerIdleTime： 表示多长时间没有读，就会发送一个心跳检测包，检测是否还处于连接状态
                            // long writerIdleTime： 表示多长时间没有写，就会发送一个心跳检测包，检测是否还处于连接状态
                            // long allIdleTime：    表示多长时间没有读写操作，就会发送一个心跳检测包，检测是否处于连接状态
                            // 最后一个参数是当前时间的单位，秒或分钟或小时。



                            // 源码表示当前处理器类是表示多长时间内没有读、没有写、或者没有读写操作，就会触发IdleStateEvent事件
                            //Triggers an IdleStateEvent when a Channel has not performed read, write, or both operation for a while.

                            //当IdleStateEvent事件 触发后， 就会传递给管道的下一个handler处理
                            //通过调用（触发）handler的 userEventTiggered 在该方法中处理 当IdleStateEvent事件

                            pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));

                            // 在我们之前的例子中处理器有监听是否关闭连接的方法，为什么还需要心跳机制？
                            // 因为我们在一些场景下自定义的处理器是无法监听到是否关闭连接的，这就导致了客户端一直占用资源，从而造成阻塞，有了心跳机制每隔几秒
                            // 后会去监听该客户端是否处于活跃状态，在指定的时间内是否有读、写操作，如果有，那就证明了 客户端处于活跃状态，如果没有，则断开与客户端的连接
                            // 进而去分配给其它的活跃客户端。

                            //添加自定义心跳处理器
                            pipeline.addLast(new HeartbeatServerHandler());
                        }
                    });

            //启动服务器
            ChannelFuture channelFuture = serverBootstrap.bind(8000).sync();
            //关闭
            channelFuture.channel().closeFuture().sync();
        } finally {
            //关闭线程
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
