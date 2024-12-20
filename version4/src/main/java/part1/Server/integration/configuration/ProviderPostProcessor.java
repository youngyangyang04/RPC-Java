package part1.Server.integration.configuration;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import part1.Server.integration.RpcService;
import part1.Server.provider.ServiceProvider;
import part1.Server.server.RpcServer;
import part1.Server.server.impl.NettyRPCRPCServer;

/**
 * @Author: yty
 * @Description: 提供者后置处理器
 * @DateTime: 2024/11/07 23:14
 **/
@Slf4j
@Configuration
public class ProviderPostProcessor implements InitializingBean,BeanPostProcessor, EnvironmentAware{

    // TODO 写死本机
    private static final ServiceProvider serviceProvider = new ServiceProvider("127.0.0.1",9999);

    private final RpcServer rpcServer = new NettyRPCRPCServer(serviceProvider);

    /**
     * 服务注册
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        // 找到bean上带有 RpcService 注解的类
        RpcService rpcService = beanClass.getAnnotation(RpcService.class);
        if (rpcService == null){
            return bean;
        }
        String group = rpcService.group();
        String version = rpcService.version();
        log.info("获取提供方服务现实bean的实现类类{},组号{},版本号{}",beanClass,group,version);
        serviceProvider.provideServiceInterface(bean, rpcService);
        return bean;
    }

    /**
     * 读取配置文件后的操作 ——> 启动 RPC 服务
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Thread t = new Thread(() -> {
            try {
                log.info("服务端启动线程");
                rpcServer.start(9999);
            } catch (Exception e) {
                log.error("启动RPC失败", e);
            }
        });
        t.setDaemon(true);
        t.start();

    }

    /**
     * 读取配置文件
     * @param environment
     */
    @Override
    public void setEnvironment(Environment environment) {
    }
}