package com.wanshi.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;


public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {


    private int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //使用客户端循环发送10条数据
        for (int i = 0; i < 5; i++) {
            String message = "今天天气很暖和，阳光明媚~  " + i;
            byte[] content = message.getBytes(Charset.forName("utf-8"));
            int length = message.getBytes(Charset.forName("utf-8")).length;

            //创建协议包
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(length);
            messageProtocol.setContent(content);

            //发送数据
            ctx.writeAndFlush(messageProtocol);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        int len = msg.getLen();
        byte[] content = msg.getContent();

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("客户端读取到信息：");
        System.out.println("长度：" + len);
        System.out.println("内容：" + new String(content, Charset.forName("utf-8")));
        System.out.println("客户端接受到消息的数量：" + (++this.count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常消息=" + cause.getMessage());
        ctx.close();
    }

}
