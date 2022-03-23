package com.wanshi.nio;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws Exception{
        //创建ServerSocketChannel， --> ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //得到一个Selector对象
        Selector selector = Selector.open();
        //绑定一个端口6666，在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //把serverSocketChannel 注册到Selector 关心事件：op_accept 连接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待客户端连接
        while (true) {
            //等待1秒，1秒后如果没有事件发生，就继续
            if (selector.select(1000) == 0) {
                System.out.println("服务器等待了1秒，无连接");
                continue;
            }

            //如果返回的>0，就获取到相关的selection集合
            //1.如果返回>0，表示已经获取到了关注的事件
            //2.selector.selectedKeys()返回关注事件的集合
            // 通过selectionKeys可以反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            // 遍历 selectionKeys，使用迭代器遍历
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()) {
                //获取到SelectionKey
                SelectionKey selectionKey = keyIterator.next();
                //根据key 对应的通道发生的事件做相应处理
                if (selectionKey.isAcceptable()) {//如果是 OP_ACCEPT
                    //给该客户端生成一个SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功，生成了一个 SocketChannel" + socketChannel.hashCode());
                    socketChannel.configureBlocking(false);
                    //将 socketChannel 注册到selector，关注事件为OP_READ，同时给socketChannel关联一个buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                //发生了OP_READ
                if (selectionKey.isReadable()) {
                    //通过selectionKey反向获取到对应channel
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    //获取到该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                    channel.read(buffer);
                    System.out.println("form 客户端 " + new String(buffer.array()));
                }

                //手动从集合中移除当前的SelectionKey，防止重复操作
                keyIterator.remove();
            }
        }
    }
}
