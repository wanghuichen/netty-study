package com.wanshi.netty.protocoltcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //解码器
        pipeline.addLast(new MessageDecoder());
        //编码器
        pipeline.addLast(new MessageEncoder());
        pipeline.addLast(new MyServerHandler());
    }
}
