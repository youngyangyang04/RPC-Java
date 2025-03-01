package com.kama.server.netty;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wxx
 * @version 1.0
 * @create 2025/2/13 15:27
 */
@Slf4j
public class HeartbeatHandler extends ChannelDuplexHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        try {
        // 处理IdleState.READER_IDLE时间
        if(evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

            IdleState idleState = ((IdleStateEvent) evt).state();
            // 如果是触发的是读空闲时间，说明已经超过n秒没有收到客户端心跳包
            if(idleState == IdleState.READER_IDLE) {
                log.info("超过10秒没有收到客户端心跳， channel: " + ctx.channel());
                // 关闭channel，避免造成更多资源占用
                ctx.close();
            }else if(idleState ==IdleState.WRITER_IDLE){
                log.info("超过20s没有写数据,channel: " + ctx.channel());
                // 关闭channel，避免造成更多资源占用
                ctx.close();
            }
        }}catch (Exception e){
            log.error("处理事件发生异常"+e);
        }
    }
}