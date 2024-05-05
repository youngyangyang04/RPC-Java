package part3.Server;


import lombok.extern.slf4j.Slf4j;
import part3.Server.server.impl.NettyRPCRPCServer;
import part3.common.service.Impl.UserServiceImpl;
import part3.common.service.UserService;
import part3.Server.provider.ServiceProvider;
import part3.Server.server.RpcServer;
import part3.Server.server.impl.SimpleRPCRPCServer;

/**
 * @author wxx
 * @version 1.0
 * @create 2024/2/11 19:39
 */

public class TestServer {
    public static void main(String[] args) {
        UserService userService=new UserServiceImpl();

        ServiceProvider serviceProvider=new ServiceProvider("127.0.0.1",9999);
        serviceProvider.provideServiceInterface(userService);

        RpcServer rpcServer=new NettyRPCRPCServer(serviceProvider);
        rpcServer.start(9999);
    }
}
