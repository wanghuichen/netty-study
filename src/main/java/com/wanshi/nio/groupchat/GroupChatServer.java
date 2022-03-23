package com.wanshi.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {

    //设置属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6677;

    public GroupChatServer() {
        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //监听
    public void listen() {
        try {
            System.out.println("服务器已启动~");
            while (true) {
                int count = selector.select(2000);
                if (count > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        if (key.isAcceptable()) {
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_READ);
                            System.out.println(sc.getRemoteAddress()+"已经上线啦");
                        }
                        if (key.isReadable()) {
                            //读取信息
                            readData(key);
                        }
                        iterator.remove();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //读取数据
    public void readData(SelectionKey key) {
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);
            if(count > 0) {
                String msg = new String(buffer.array());
                System.out.println("form 客户端 msg is：" + msg);
                //向其他客户端转发消息
                sendInfoToOtherClients(msg, channel);
            }
        } catch (Exception e) {
            try {
                System.out.println(channel.getRemoteAddress()+"离线了");
                key.cancel();//取消注册
                channel.close();//关闭通道
            } catch (Exception e2) {

            }
        }
    }

    //转发消息给其他用户
    private void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中...");
        //遍历注册到其他通道
        for (SelectionKey key : selector.keys()) {
            SelectableChannel targetChannel = key.channel();
            if (targetChannel instanceof  SocketChannel && targetChannel != self) {
                SocketChannel dest = (SocketChannel) targetChannel;
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
