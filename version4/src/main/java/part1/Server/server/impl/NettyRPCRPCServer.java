package part1.Server.server.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import part1.Server.netty.nettyInitializer.NettyServerInitializer;
import part1.Server.provider.ServiceProvider;
import part1.Server.server.RpcServer;

/**
 * @version 1.0
 * @create 2024/2/26 14:01
 */
@AllArgsConstructor
@Slf4j
public class NettyRPCRPCServer implements RpcServer {
    private ServiceProvider serviceProvider;

    @Override
    public void start(int port) {
        // netty 服务线程组boss负责建立连接， work负责具体的请求
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        log.info("netty服务端启动了");
        try {
            //启动netty服务器
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //初始化
            serverBootstrap.group(bossGroup,workGroup).channel(NioServerSocketChannel.class)
                    //将服务提供者传递 进行netty服务初始化，然后在channel中进行监听
                    .childHandler(new NettyServerInitializer(serviceProvider));
            //同步堵塞
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            //死循环监听
            channelFuture.channel().closeFuture().sync();
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    @Override
    public void stop() {

    }
}