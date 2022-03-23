package com.wanshi.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class MyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器接受消息：" + msg.text());

        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间：" + sdf.format(new Date()) + " -- 消息：" + msg.text()));
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdded被调用：" + ctx.channel().id().asLongText());
        System.out.println("handlerAdded被调用：" + ctx.channel().id().asShortText());
        System.out.println(sdf.format(new Date()) + "  【服务器】用户已连接！");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved被调用：" + ctx.channel().id().asLongText());
        System.out.println(sdf.format(new Date()) + "  【服务器】用户已断开连接");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("发生异常：" + cause.getMessage());
        ctx.close();
    }
}
