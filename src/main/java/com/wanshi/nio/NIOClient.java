package com.wanshi.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {
    public static void main(String[] args) throws Exception{
        //得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞模式
        socketChannel.configureBlocking(false);
        //提供服务器端ip和端口
        InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 6666);
        //连接服务器
        if (!socketChannel.connect(socketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其它工作...");
            }
        }

        //如果连接成功就发送数据
        String str = "hello 小王~";
        //将一个字节数据存入到buffer中
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        //发送数据，将buffer数据写入channel
        socketChannel.write(buffer);
        System.in.read();
    }
}
