package com.wanshi.netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyBateToLongDecoder extends ByteToMessageDecoder {

    /**
     * 解码数据
     * @param ctx 上下文
     * @param in    数据
     * @param out   List集合，讲解码后的数据传给下一个handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        // 因为 Long 8个字节
        while (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
}
