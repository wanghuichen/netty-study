package com.wanshi.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MessageDecoder extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //解码
        System.out.println("MessageDecoder 的decode被调用");
        //将得到的二进制字节码  -> MessageProtocol 数据包（对象）
        int length = in.readInt();
        byte[] content = new byte[length];
        in.readBytes(content);

        //封装成 MessageProtocol对象，放入out中，传给下一个handler进行业务处理
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(length);
        messageProtocol.setContent(content);

        out.add(messageProtocol);
    }
}
