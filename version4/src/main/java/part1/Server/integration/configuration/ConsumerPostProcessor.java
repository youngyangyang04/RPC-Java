package part1.Server.integration.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import part1.Client.proxy.ClientProxy;
import part1.Server.integration.References;

import java.lang.reflect.Field;

/**
 * @Author: yty
 * @Description: 消费者后置处理器
 * @DateTime: 2024/11/08 16:02
 **/
@Slf4j
@Configuration
public class ConsumerPostProcessor implements InitializingBean, BeanPostProcessor, EnvironmentAware {

    // 代理对象
//    private static final ClientProxy clientProxy;
//
//    static {
//        try {
//            clientProxy = new ClientProxy();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }


    /**
     * 读取配置文件后的操作 ——> 启动 RPC 服务
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
    }

    /**
     * 获取代理对象，发生请求
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 获取所有字段
        final Field[] fields = bean.getClass().getDeclaredFields();
        // 遍历所有字段找到 RpcReference 注解的字段
        for (Field field : fields) {
            if(field.isAnnotationPresent(References.class)){
                final References rpcReference = field.getAnnotation(References.class);
                final Class<?> aClass = field.getType();
                field.setAccessible(true);
                Object object = null;
                try {
                    ClientProxy clientProxy = new ClientProxy();
                    //获取服务代理对象
                    log.info("接口类型:{},接口参数:{}",aClass,rpcReference);
                    object = clientProxy.getProxy(aClass,rpcReference);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    // 将消费者的原接口 替换为 RPC的代理对象
                    field.set(bean,object);
                    field.setAccessible(false);
                    log.info(beanName + " field:" + field.getName() + "注入成功");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    log.info(beanName + " field:" + field.getName() + "注入失败");
                }
            }
        }
        return bean;
    }

    /**
     * 读取配置文件
     * @param environment
     */
    @Override
    public void setEnvironment(Environment environment) {
    }
}