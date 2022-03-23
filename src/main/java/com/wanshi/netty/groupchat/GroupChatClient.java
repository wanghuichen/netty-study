package com.wanshi.netty.groupchat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class GroupChatClient {

    //定义属性
    private final String host;
    public final int port;


    public GroupChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run () {
        EventLoopGroup eventExecutors = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //得到pipeline
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //加入相关的handler
                            pipeline.addLast("decoder", new StringDecoder());
                            pipeline.addLast("encoder", new StringEncoder());
                            //加入自定义handler
                            pipeline.addLast(new GroupChatClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            //得到channel
            Channel channel = channelFuture.channel();
            System.out.println("-----" + channel.localAddress() + "----");
            //客户端需要输入信息，创建一个扫描器
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String msg = scanner.nextLine();
                //通过channel发送到服务器端
                channel.writeAndFlush(msg+"\r\n");
            }
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {

        } finally {
            eventExecutors.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        GroupChatClient groupChatClient = new GroupChatClient("127.0.0.1", 7000);
        groupChatClient.run();
    }
}
