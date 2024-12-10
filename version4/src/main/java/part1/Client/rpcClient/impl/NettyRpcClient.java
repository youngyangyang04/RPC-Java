package part1.Client.rpcClient.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import part1.Client.netty.nettyInitializer.NettyClientInitializer;
import part1.Client.rpcClient.RpcClient;
import part1.common.Message.RpcRequest;
import part1.common.Message.RpcResponse;
import part1.Client.serviceCenter.ServiceCenter;

import java.net.InetSocketAddress;

/**
 * @author wxx
 * @version 1.0
 * @create 2024/5/2 19:40
 */
@Slf4j
public class NettyRpcClient implements RpcClient {

    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;

    private ServiceCenter serviceCenter;
    public NettyRpcClient(ServiceCenter serviceCenter) throws InterruptedException {
        this.serviceCenter=serviceCenter;
    }

    //netty客户端初始化
    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer());
    }
    @Override
    public RpcResponse sendRequest(RpcRequest request) {
        log.info("客户端发生请求" + request);
        //从注册中心获取host,post
        InetSocketAddress address = serviceCenter
                .serviceDiscovery(request.getInterfaceName() + "." +request.getReferences().version());
        String host = address.getHostName();
        int port = address.getPort();
        try {
            ChannelFuture channelFuture  = bootstrap.connect(host, port).sync();
            Channel channel = channelFuture.channel();
            // 发送数据
            channel.writeAndFlush(request);
            //sync()堵塞获取结果
            channel.closeFuture().sync();
            // 阻塞的获得结果，通过给channel设计别名，获取特定名字下的channel中的内容（这个在hanlder中设置）
            // AttributeKey是，线程隔离的，不会由线程安全问题。
            // 当前场景下选择堵塞获取结果
            // 其它场景也可以选择添加监听器的方式来异步获取结果 channelFuture.addListener...
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("RPCResponse");
            RpcResponse response = channel.attr(key).get();

            log.info("完成请求——请求返回结果（客户端接受）" + response);
            return response;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
