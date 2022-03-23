package com.wanshi.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 自定义一个Handler，需要继承netty规定好的某个HandlerAdapter
 * 这时我们自定义的handler才能称为一个handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    //读取数据事件（这里我们可以读取客户端发送的消息）



    /**
     * 1.ChannelHandlerContext ctx： 上下文对象，含有 管道pipeline，通道channel，地址
     * 2.Object msg：就是客户端发送的数据，默认Object
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx =" + ctx);
        //将 msg 转成一个ByteBuf
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送消息是：" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址：" + ctx.channel().remoteAddress());

//        //自定义普通任务队列，将耗时长的任务加入队列，定义到NioEventLoop --> taskQueue
//        ctx.channel().eventLoop().execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.currentThread().sleep(10 * 1000);
//                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端：喵2~", CharsetUtil.UTF_8));
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        ctx.channel().eventLoop().execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.currentThread().sleep(20 * 1000);
//                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端：喵3~", CharsetUtil.UTF_8));
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//
//        //用户自定义定时任务 --》 该任务是提交到 scheduleQueue中
//        ctx.channel().eventLoop().schedule(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.currentThread().sleep(5 * 1000);
//                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端：喵4~", CharsetUtil.UTF_8));
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, 5, TimeUnit.SECONDS);
//
//        System.out.println("go ~");
    }

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
