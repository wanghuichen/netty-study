package com.wanshi.nio.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NIOClient {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 6060));
        String fileName = "d:\\file1.zip";

        //得到一个文件channel
        FileChannel fileChannel = new FileInputStream(fileName).getChannel();

        //准备发送
        long startTimes = System.currentTimeMillis();

        //在Linux下一个transferTo方法就可以完成传送
        //在windows下 一次调用transferTo只能发送8m，就需要分段传输文件，而且要注意传输时的位置

        //transferTo底层使用到零拷贝
        long transferCount = 0;
        long size = fileChannel.size() / 1024;
        boolean isAdd = size != 0 ? true : false;
        if (isAdd) {
            size += 1;
        }
        for (int i = 0; i < size; i++) {
            long l = fileChannel.size() / (1024*8);
            transferCount += l;
            fileChannel.transferTo(i, l, socketChannel);
        }

        System.out.println("发送的总的字节数 = " + transferCount + "，总耗时：" + (System.currentTimeMillis() - startTimes));
        //关闭通道
        fileChannel.close();
    }
}
