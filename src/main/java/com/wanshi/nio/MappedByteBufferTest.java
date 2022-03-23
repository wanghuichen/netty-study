package com.wanshi.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedByteBufferTest {

    public static void main(String[] args) throws Exception{
        RandomAccessFile randomAccessFile = new RandomAccessFile("d:\\01.txt", "rw");
        //获取对应的通道
        FileChannel channel = randomAccessFile.getChannel();

        /**
         * 参数1：使用的读写模式
         * 参数2：可以直接修改的起始位置
         * 参数3：映射到内存的大小（不是索引位置），可以直接修改的范围是0-5，不等于5，是根据索引修改的
         * 实际类型 DirectByteBuffer
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0, (byte) 'Q');
        mappedByteBuffer.put(2, (byte) '9');
        //关闭流
        randomAccessFile.close();
    }
}
