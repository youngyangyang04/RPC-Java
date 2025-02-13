package com.kama.server.netty;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author wxx
 * @version 1.0
 * @create 2025/2/13 15:27
 */
public class HeartbeatHandler extends ChannelDuplexHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 处理IdleState.READER_IDLE时间
        if(evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

            IdleState idleState = ((IdleStateEvent) evt).state();

            // 如果是触发的是读空闲时间，说明已经超过n秒没有收到客户端心跳包
            if(idleState == IdleState.READER_IDLE) {
                System.out.println("超过n秒没有收到客户端心跳， channel: " + ctx.channel());

                // 关闭channel，避免造成更多资源占用
                ctx.close();
            }

        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("接收到客户端数据， channel: " + ctx.channel() + ", 数据： " + msg.toString());
    }
}