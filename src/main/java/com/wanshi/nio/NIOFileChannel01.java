package com.wanshi.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel01 {

    public static void main(String[] args) throws Exception{
        String str = "Hello 小王！";
        //创建一个输出流->channel
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\01.txt");

        //通过输出流获取对应的FileChannel
        //这个fileChannel的真实类型是FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();

        //创建一个缓冲区 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //将str放入到byteBuffer中
        byteBuffer.put(str.getBytes());

        //对byteBuffer进行反转
        byteBuffer.flip();

        //将byteBuffer 数据写入FileChannel
        fileChannel.write(byteBuffer);

        //关闭流
        fileOutputStream.close();
    }
}
