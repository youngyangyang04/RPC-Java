package com.kama.client.netty;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author wxx
 * @version 1.0
 * @create 2025/2/13 15:01
 */
public class HeartbeatHandler extends ChannelDuplexHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

            IdleState idleState = idleStateEvent.state();

            if(idleState == IdleState.WRITER_IDLE) {
                ctx.writeAndFlush("两秒没有写数据，发送心跳包\n");
                System.out.println("超过两秒没有写数据，发送心跳包");
            }

        }
    }
}
