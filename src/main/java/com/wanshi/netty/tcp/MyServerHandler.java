package com.wanshi.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] buf = new byte[msg.readableBytes()];
        msg.readBytes(buf);

        //将buf转成字符串
        String message = new String(buf, Charset.forName("utf-8"));
        System.out.println("服务器端接受数据：" + message);
        System.out.println("服务器端接受到消息量：" + (++this.count));

        //服务器回送数据给客户端，回送一个随机id
        ByteBuf byteBuf = Unpooled.copiedBuffer(UUID.randomUUID().toString() + ",", Charset.forName("utf-8"));
        ctx.writeAndFlush(byteBuf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
