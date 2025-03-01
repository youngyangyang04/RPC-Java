package com.kama.server.netty;


import com.kama.server.provider.ServiceProvider;
import common.serializer.mycoder.MyDecoder;
import common.serializer.mycoder.MyEncoder;
import common.serializer.myserializer.Serializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.AllArgsConstructor;

import java.util.concurrent.TimeUnit;


/**
 * @ClassName NettyServerInitializer
 * @Description 服务端初始化器
 * @Author Tong
 * @LastChangeDate 2024-12-02 10:55
 * @Version v5.0
 */
@AllArgsConstructor
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
    private ServiceProvider serviceProvider;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 服务端关注读事件和写事件，如果10秒内没有收到客户端的消息，将会触发IdleState.READER_IDLE事件，将由HeartbeatHandler进行处理
        pipeline.addLast(new IdleStateHandler(10, 20, 0 ,TimeUnit.SECONDS));
        pipeline.addLast(new HeartbeatHandler());
        //使用自定义的编/解码器
        pipeline.addLast(new MyEncoder(Serializer.getSerializerByCode(3)));
        pipeline.addLast(new MyDecoder());
        pipeline.addLast(new NettyRpcServerHandler(serviceProvider));

    }
}
