package com.wanshi.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Author: tz_wl
 * Date: 2020/9/20 21:41
 * Content:
 *
 * //客户端
 //链接服务器
 //接收消息
 //发送消息
 */
public class Two {

    //定义 用到的变量
    private final String HOST = "127.0.0.1";
    private final int PORT = 8095;
    private Selector selector;
    private SocketChannel socketChannel;
    private String userName;


    //构造函数
    public Two() throws IOException {
        selector =Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", PORT));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        userName = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(userName+ "  is Starting  ...");
    }


    //向服务器发送消息
    public void sendInfo(String info) {
        info = userName + "说：" + info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //读取从服务器 回复的消息
    public void readInfo(){
        try{
            int readChannels = selector.select();
            if(readChannels > 0){
                Iterator<SelectionKey> iterator =selector.selectedKeys().iterator();
                while(iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    //此处client只有 readable 没有 acceptable
                    if(key.isReadable()){
                        SocketChannel sc = (SocketChannel)key.channel();
                        ByteBuffer buffer =ByteBuffer.allocate(1034);
                        sc.read(buffer);
                        //
                        String msg = new String (buffer.array());
                        System.out.println(msg);
                    }

                    iterator.remove();  //读取后，别忘了删除当前 selectionKey
                }
            }else{
                System.out.println("没有可用通道");
            }
        }catch(Exception e){
        }
    }

    //  本线程   监听 System.in  nextLine  发送数据给服务端
    //    // //启动一个线程  每隔 3 秒 ，读取从服务器发送的数据
    public static void main(String[] args) throws Exception {

        Two chatClient = new Two();
        //启动一个线程  每隔 3 秒 ，读取从服务器发送的数据
        new Thread() {
            public void run() {
                while (true) {
                    chatClient.readInfo();

                    try {
                        Thread.currentThread().sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        // 发送数据给服务端
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            chatClient.sendInfo(s);
        }
    }
}

