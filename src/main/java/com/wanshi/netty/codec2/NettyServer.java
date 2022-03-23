package com.wanshi.netty.codec2;

import com.wanshi.netty.codec.StudentPOJO;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

public class NettyServer {

    public static void main(String[] args) throws Exception {
        // 创建BossGroup 和 WorkerGroup
        //说明
        //1.创建2个线程组，分别是boosGroup和workerGroup
        //2.boosGroup只是处理连接请求，真正的与客户端业务处理，会交给workerGroup完成
        //3.两个都是无限循环
        //4. boosGroup 和 workerGroup 含有的子线程（NioEventLoop）的个数
        // 默认实际 CPU核数*2
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //创建服务器端的启动的对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            //使用链式编程来进行设置
            bootstrap.group(boosGroup, workerGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class) //使用NioServerSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列等待连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 创建一个通道初始化对象（匿名对象）
                        //给pipeline 设置处理器
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //加入解码器，指定对那种对象进行解码
                            pipeline.addLast("decoder", new ProtobufDecoder(MyDataInfo.MyMessage.getDefaultInstance()));
                            //可以使用一个集合管理SocketChannel，再推送消息时，可以将业务加入到各个channel对应的NioEventLoop的taskQueue
                            //或者 scheduleTaskQueue
                            System.out.println("客户 SocketChannel：" + socketChannel.hashCode());
                            pipeline.addLast(new NettyServerHandler());
                        }
                    }); //给我们的workerGroup的某一个EventLoop的对应的管道设置处理器

            System.out.println("服务器 is ready...");

            //绑定一个端口并且同步，生成了一个ChannelFuture对象
            //启动服务器并绑定端口
            ChannelFuture channelFuture = bootstrap.bind(6668).sync();
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("监听端口 6668 成功");
                    } else {
                        System.out.println("监听端口 6668 失败");

                    }
                }
            });
            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //优雅关闭
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }
}
