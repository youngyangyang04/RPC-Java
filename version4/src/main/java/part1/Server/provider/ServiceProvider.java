package part1.Server.provider;

import lombok.extern.slf4j.Slf4j;
import part1.Server.integration.RpcService;
import part1.Server.ratelimit.provider.RateLimitProvider;
import part1.Server.serviceRegister.impl.ZKServiceRegister;
import part1.Server.serviceRegister.ServiceRegister;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wxx
 * @version 1.0
 * @create 2024/2/16 17:35
 */
@Slf4j
public class ServiceProvider {
    private Map<String,Object> interfaceProvider;

    private int port;
    private String host;
    //注册服务类
    private ServiceRegister serviceRegister;
    //限流器
    private RateLimitProvider rateLimitProvider;
    public ServiceProvider(String host,int port){
        //需要传入服务端自身的网络地址
        this.host=host;
        this.port=port;
        this.interfaceProvider=new HashMap<>();
        this.serviceRegister=new ZKServiceRegister();
        this.rateLimitProvider=new RateLimitProvider();
    }

    public void provideServiceInterface(Object service, RpcService rpcService){
        String serviceName=service.getClass().getName();
        Class<?>[] interfaceName=service.getClass().getInterfaces();
        String version = rpcService.version();
        boolean canRetry = rpcService.canRetry();
        for (Class<?> clazz:interfaceName){
            String serviceAndVersion = clazz.getName() + "." + version;
            //本机的映射表（接口名+版本号 : 实现类）
            interfaceProvider.put(serviceAndVersion,service);
            log.info("本机映射表存储{}:{}", serviceAndVersion,service);
            //在注册中心注册服务 （注册中心是用于注册 服务——服务地址，是用来给客户端返回服务地址的）
            serviceRegister.register(serviceAndVersion,new InetSocketAddress(host,port),rpcService);
        }
    }

    public Object getService(String interfaceName){
        return interfaceProvider.get(interfaceName);
    }

    public RateLimitProvider getRateLimitProvider(){
        return rateLimitProvider;
    }
}
