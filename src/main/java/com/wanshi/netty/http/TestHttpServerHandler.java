package com.wanshi.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * 1. SimpleChannelInboundHandler 是ChannelInboundHandlerAdapter的子类
 * 2. HttpObject 表示客户端和服务器端相互通讯的数据被封装成HttpObject类型
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    //读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {

        //判断httpObject是不是 httprequest请求
        if (httpObject instanceof HttpRequest) {

            System.out.println("msg 类型：" + httpObject.getClass());
            System.out.println("客户端地址：" + channelHandlerContext.channel().remoteAddress());


            HttpRequest httpRequest = (HttpRequest) httpObject;
            //获取uri
            URI uri = new URI(httpRequest.uri());
            //过滤指定资源
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求了favicon.ico，不做响应");
                return;
            }

            //回复信息给浏览器 [http协议]

            ByteBuf content = Unpooled.copiedBuffer("hello，我是服务器", CharsetUtil.UTF_8);
            //构造一个http的响应即  httprepanse
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            //将构建好的 response 返回
            channelHandlerContext.writeAndFlush(response);
        }
    }
}
