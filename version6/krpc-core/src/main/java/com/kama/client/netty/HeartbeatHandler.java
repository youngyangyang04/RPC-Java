package com.kama.client.netty;

import common.message.RpcRequest;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.lang.ref.ReferenceQueue;

/**
 * @author wxx
 * @version 1.0
 * @create 2025/2/13 15:01
 */
@Slf4j
public class HeartbeatHandler extends ChannelDuplexHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

            IdleState idleState = idleStateEvent.state();

            if(idleState == IdleState.WRITER_IDLE) {
                ctx.writeAndFlush(RpcRequest.heartBeat());
                log.info("超过8秒没有写数据，发送心跳包");
            }
        }else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
