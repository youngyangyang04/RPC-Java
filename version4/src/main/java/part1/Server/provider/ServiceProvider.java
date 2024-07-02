package part1.Server.provider;

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

    public void provideServiceInterface(Object service,boolean canRetry){
        String serviceName=service.getClass().getName();
        Class<?>[] interfaceName=service.getClass().getInterfaces();

        for (Class<?> clazz:interfaceName){
            //本机的映射表
            interfaceProvider.put(clazz.getName(),service);
            //在注册中心注册服务
            serviceRegister.register(clazz.getName(),new InetSocketAddress(host,port),canRetry);
        }
    }

    public Object getService(String interfaceName){
        return interfaceProvider.get(interfaceName);
    }

    public RateLimitProvider getRateLimitProvider(){
        return rateLimitProvider;
    }
}
