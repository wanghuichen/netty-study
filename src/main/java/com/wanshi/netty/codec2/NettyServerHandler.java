package com.wanshi.netty.codec2;

import com.wanshi.netty.codec.StudentPOJO;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 自定义一个Handler，需要继承netty规定好的某个HandlerAdapter
 * 这时我们自定义的handler才能称为一个handler
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {

    //读取数据事件（这里我们可以读取客户端发送的消息）
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {

        //根据DataType来显示不同的信息，
        MyDataInfo.MyMessage.DataType dataType = msg.getDataType();
        if (dataType == MyDataInfo.MyMessage.DataType.StudentType) {
            MyDataInfo.Student student = msg.getStudent();
            System.out.println("学生id=" + student.getId() + "，学生名字=" + student.getName());
        } else if (dataType == MyDataInfo.MyMessage.DataType.WorkerType) {
            MyDataInfo.Worker worker = msg.getWorker();
            System.out.println("工人名字：" + worker.getName() + "，工人年龄：" + worker.getAge());
        } else {
            System.out.println("传输的类型不正确！");
        }

        ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端：喵4~", CharsetUtil.UTF_8));
    }

//    /**
//     * 1.ChannelHandlerContext ctx： 上下文对象，含有 管道pipeline，通道channel，地址
//     * 2.Object msg：就是客户端发送的数据，默认Object
//     * @param ctx
//     * @param msg
//     * @throws Exception
//     */
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("server ctx =" + ctx);
//        //将 msg 转成一个ByteBuf
//        ByteBuf buf = (ByteBuf) msg;
//        System.out.println("客户端发送消息是：" + buf.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端地址：" + ctx.channel().remoteAddress());
//
////        //自定义普通任务队列，将耗时长的任务加入队列，定义到NioEventLoop --> taskQueue
////        ctx.channel().eventLoop().execute(new Runnable() {
////            @Override
////            public void run() {
////                try {
////                    Thread.currentThread().sleep(10 * 1000);
////                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端：喵2~", CharsetUtil.UTF_8));
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
////            }
////        });
////
////        ctx.channel().eventLoop().execute(new Runnable() {
////            @Override
////            public void run() {
////                try {
////                    Thread.currentThread().sleep(20 * 1000);
////                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端：喵3~", CharsetUtil.UTF_8));
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
////            }
////        });
////
////
////        //用户自定义定时任务 --》 该任务是提交到 scheduleQueue中
////        ctx.channel().eventLoop().schedule(new Runnable() {
////            @Override
////            public void run() {
////                try {
////                    Thread.currentThread().sleep(5 * 1000);
////                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端：喵4~", CharsetUtil.UTF_8));
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
////            }
////        }, 5, TimeUnit.SECONDS);
////
////        System.out.println("go ~");
//    }



    /**
     * 数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //writeAndFlush 是 write+flush
        //将数据写入到缓存，并刷新
        //一般讲，需要对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端：喵1~", CharsetUtil.UTF_8));
    }

    //处理异常，一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }



}
