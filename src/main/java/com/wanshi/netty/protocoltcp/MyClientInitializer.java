package com.wanshi.netty.protocoltcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //加入编码器
        pipeline.addLast(new MessageEncoder());
        //加入解码器
        pipeline.addLast(new MessageDecoder());
        //加入处理器
        pipeline.addLast(new MyClientHandler());
    }

}
