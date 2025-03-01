package com.kama.client.netty;


import common.serializer.mycoder.MyDecoder;
import common.serializer.mycoder.MyEncoder;
import common.serializer.myserializer.Serializer;
import common.trace.TraceContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.MDC;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName NettyClientInitializer
 * @Description 配置自定义的编码器以及Handler
 * @Author Tong
 * @LastChangeDate 2024-12-02 10:16
 * @Version v5.0
 */
@Slf4j
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    public NettyClientInitializer(){}
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 使用自定义的编码器和解码器
        try {
            // 根据传入的序列化器类型初始化编码器
            pipeline.addLast(new MyEncoder(Serializer.getSerializerByCode(3)));
            pipeline.addLast(new MyDecoder());
            pipeline.addLast(new NettyClientHandler());
            pipeline.addLast(new MDCChannelHandler());
            // 客户端只关注写事件，如果超过8秒没有发送数据，则发送心跳包
            pipeline.addLast(new IdleStateHandler(0, 8, 0, TimeUnit.SECONDS));
            pipeline.addLast(new HeartbeatHandler());
            log.info("Netty client pipeline initialized with serializer type: {}",Serializer.getSerializerByCode(3).getType());
        } catch (Exception e) {
            log.error("Error initializing Netty client pipeline", e);
            throw e;  // 重新抛出异常，确保管道初始化失败时处理正确
        }
    }
}
