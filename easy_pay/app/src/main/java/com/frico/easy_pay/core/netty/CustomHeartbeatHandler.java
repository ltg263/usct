package com.frico.easy_pay.core.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author zhangyou
 * @date 2019/5/9
 */
public abstract class CustomHeartbeatHandler<T> extends SimpleChannelInboundHandler<T> {
    private int heartbeatCount = 0;

    public CustomHeartbeatHandler() {
    }

    /**
     * 发送心跳请求数据
     *
     * @param context
     */
    protected void sendPingMsg(ChannelHandlerContext context) {
        //建议  发送 目前用户的id
        context.writeAndFlush(getPingMsg() + "\n");
        heartbeatCount++;
        System.out.println(context.channel().remoteAddress() + ", count: " + heartbeatCount);
    }

    /**
     * 发送心跳回应数据
     *
     * @param context
     */
    protected void sendPongMsg(ChannelHandlerContext context) {
        context.writeAndFlush(getPongMsg());
        heartbeatCount++;
        System.out.println(context.channel().remoteAddress() + ", count: " + heartbeatCount);
    }

    protected abstract <T> T getPingMsg();


    protected abstract <T> T getPongMsg();

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // IdleStateHandler 所产生的 IdleStateEvent 的处理逻辑.
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                //一段时间内没有数据接收
                case READER_IDLE:
                    handleReaderIdle(ctx);
                    break;
                //一段时间内没有数据发送
                case WRITER_IDLE:
                    handleWriterIdle(ctx);
                    break;
                //一段时间内没有数据接收或发送
                case ALL_IDLE:
                    handleAllIdle(ctx);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.err.println("---" + ctx.channel().remoteAddress() + " is active---");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.err.println("---" + ctx.channel().remoteAddress() + " is inactive---");
    }

    protected void handleReaderIdle(ChannelHandlerContext ctx) {
        System.err.println("--READER_IDLE--- 读");
        //长期没收到服务器推送数据
    }

    protected void handleWriterIdle(ChannelHandlerContext ctx) {
        System.err.println("--WRITER_IDLE--- 写");
        //很久没发送消息了，就发送一条心跳
        ctx.writeAndFlush(getPingMsg());
    }

    protected void handleAllIdle(ChannelHandlerContext ctx) {
        System.err.println("--ALL_IDLE--- 全");
    }

}
