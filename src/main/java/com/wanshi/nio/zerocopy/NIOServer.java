package com.wanshi.nio.zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NIOServer {

    public static void main(String[] args) throws IOException {
        InetSocketAddress address = new InetSocketAddress(6060);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        ServerSocket serverSocket = serverSocketChannel.socket();

        serverSocket.bind(address);

        ByteBuffer byteBuffer = ByteBuffer.allocate(5060);

        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();

            int readCount = 0;
            while (-1 != readCount) {
                try {
                    readCount = socketChannel.read(byteBuffer);
                } catch (Exception e) {
//                    e.printStackTrace();
                    break;
                }

                byteBuffer.rewind();//倒带 buffer position = 0, mark 作废
            }
        }
    }
}
