
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Author: tz_wl
 * Date: 2020/9/20 21:41
 * Content:
 *
 * 1.1  服务器启动并监听 * 端口
 1.2  打印上线信息
 1.2  打印数据信息
 1.3  打印转发消息
 1.4  打印离线消息
 *
 *
 */
public class One {

    //01 定义 需要都用到的变量
    private Selector selector ;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 8095;

    //02  构造函数 初始化    注册 serverSocketChannel 到 Selector
    public One() throws  Exception{
        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector , SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // 03  服务端 监听函数
    //      isAcceptable     有客户端连接上来 打印 ***客户端连接到服务器了
    //      isReadable       客户端发送的数据 放到单独方法  readData  中处理
    public void listen(){
        try{
            System.out.println("服务端已经启动 ...  ");
            while(true){
                int count= selector.select(2000);
                if(count>0){
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while(iterator.hasNext()){
                        SelectionKey key= iterator.next();
                        if(key.isAcceptable()){
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false) ;            //设置客户端非阻塞
                            sc.register(selector,SelectionKey.OP_READ);
                            //提示 某某客户端  上线了
                            System.out.println(sc.getRemoteAddress()+"上线");
                        }
                        if(key.isReadable()){
                            // 专门方法处理读事件
                            readData(key);
                        }
                        iterator.remove();// 注意要删除 别忘记
                    }
                }else{
                    //System.out.println("waiting ...");
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
        }
    }

    //读取客户端消息
    // 读到数据了  提示 有客户端发来消息
    // 其他客户端转发 消息
    // 有客户端离线了，提示  离线了
    private void readData(SelectionKey key){
        SocketChannel channel = null;
        try{
            channel= (SocketChannel) key.channel();
            ByteBuffer buffer=ByteBuffer.allocate(1024);
            int count = channel.read(buffer);
            if(count>0){
                String msg = new String(buffer.array());
                System.out.println("from 客户端  msg is:" + msg );
                //  向 其他客户端转发消息  专用方法
                sendInfoToOtherClients(msg,channel);
            }

        } catch (Exception e){
            try{
                System.out.println(channel.getRemoteAddress()+"离线了");
                key.cancel();        //取消注册
                channel.close();     //关闭通道
            }catch(IOException e2){

            }
        }
    }


    //转发消息给其他客户
    private void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中... ");
        //遍历 所有 注册到 selector 上面的socketChannel -  self
        for(SelectionKey key: selector.keys()){
            Channel targetChannel = key.channel();
            //如果就一个服务器  只要  targetChannel != self 就可以
            //考虑到多台服务器 if(targetChannel instanceof SocketChannel )
            if(targetChannel instanceof SocketChannel && targetChannel != self)
            {
                SocketChannel dest = (SocketChannel) targetChannel;
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        One one = new One();
        one.listen();
    }

}

