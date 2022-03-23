package com.wanshi.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel03 {
    public static void main(String[] args) throws Exception{
        //创建输入流
        FileInputStream fileInputStream = new FileInputStream("d:\\01.txt");
        //获取输入流对应的FileChannel
        FileChannel fileChannel01 = fileInputStream.getChannel();

        //创建输出流
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\02.txt");
        FileChannel fileChannel02 = fileOutputStream.getChannel();

        //创建缓冲区Bytebuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        while (true) {
            //这里有一个重要的操作，清空，重置缓冲区属性
            byteBuffer.clear();

            //读取文件
            int read = fileChannel01.read(byteBuffer);
            if (read == -1) {
                break;
            }
            //反转缓冲区为写入
            byteBuffer.flip();
            //写入文件
            fileChannel02.write(byteBuffer);
        }
    }
}
