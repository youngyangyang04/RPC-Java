package com.kama.client.netty;

import common.message.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName NettyClientHandler
 * @Description 客户端处理器
 * @Author Tong
 * @LastChangeDate 2024-12-02 10:15
 * @Version v5.0
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        // 接收到response, 给channel设计别名，让sendRequest里读取response
        AttributeKey<RpcResponse> RESPONSE_KEY = AttributeKey.valueOf("RPCResponse");
        // 将响应存入 Channel 属性
        ctx.channel().attr(RESPONSE_KEY).set(response);
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Channel exception occurred", cause);
        ctx.close();
    }
}
