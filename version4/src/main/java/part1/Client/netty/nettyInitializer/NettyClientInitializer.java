package part1.Client.netty.nettyInitializer;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import part1.Client.netty.handler.NettyClientHandler;
import part1.common.serializer.myCode.MyDecoder;
import part1.common.serializer.myCode.MyEncoder;
import part1.common.serializer.mySerializer.JsonSerializer;

/**
 * @author wxx
 * @version 1.0
 * @create 2024/2/26 17:26
 */
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //使用自定义的编/解码器
        pipeline.addLast(new MyEncoder(new JsonSerializer()));
        pipeline.addLast(new MyDecoder());
        pipeline.addLast(new NettyClientHandler());
    }
}
