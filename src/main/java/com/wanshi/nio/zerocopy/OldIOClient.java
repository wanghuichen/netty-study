package com.wanshi.nio.zerocopy;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class OldIOClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 6001);

        String fileName = "D:\\file1.zip";
        FileInputStream inputStream = new FileInputStream(fileName);

        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        byte[] buffer = new byte[4096];
        long readCount;
        long total = 0;

        long startTimes = System.currentTimeMillis();

        while ((readCount = inputStream.read(buffer)) >= 0) {
            total += readCount;
            dataOutputStream.write(buffer);
        }

        System.out.println("发送总字节数：" + total + "， 耗时：" + (System.currentTimeMillis() - startTimes));

        dataOutputStream.close();
        socket.close();
        inputStream.close();
    }
}
