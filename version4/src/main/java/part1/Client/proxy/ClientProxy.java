package part1.Client.proxy;


import lombok.extern.slf4j.Slf4j;
import part1.Client.circuitBreaker.CircuitBreaker;
import part1.Client.circuitBreaker.CircuitBreakerProvider;
import part1.Client.retry.guavaRetry;
import part1.Client.rpcClient.RpcClient;
import part1.Client.rpcClient.impl.NettyRpcClient;
import part1.Client.serviceCenter.ServiceCenter;
import part1.Client.serviceCenter.ZKServiceCenter;
import part1.Server.integration.References;
import part1.common.Message.RpcRequest;
import part1.common.Message.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

/**
 * @author wxx
 * @version 1.0
 * @create 2024/2/6 16:49
 */
@Slf4j
public class ClientProxy implements InvocationHandler {
    //传入参数service接口的class对象，反射封装成一个request

    private RpcClient rpcClient;
    private ServiceCenter serviceCenter;
    private CircuitBreakerProvider circuitBreakerProvider;
    private HashMap<String , References> referencesHashMap = new HashMap<>();
    private References references;
    private Class<?> clazz;
    public ClientProxy() throws InterruptedException {
        serviceCenter=new ZKServiceCenter();
        rpcClient=new NettyRpcClient(serviceCenter);
        circuitBreakerProvider=new CircuitBreakerProvider();
    }

    //jdk动态代理，每一次代理对象调用方法，都会经过此方法增强（反射获取request对象，socket发送到服务端）
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //构建request
        RpcRequest request=RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args).paramsType(method.getParameterTypes())
                .references(references)
                .build();
        //获取熔断器
        CircuitBreaker circuitBreaker=circuitBreakerProvider.getCircuitBreaker(method.getName());
        //判断熔断器是否允许请求经过
        if (!circuitBreaker.allowRequest()){
            //这里可以针对熔断做特殊处理，返回特殊值
            return null;
        }
        //数据传输
        RpcResponse response;
        //后续添加逻辑：为保持幂等性，只对白名单上的服务进行重试
        if (serviceCenter.checkRetry(request)){
            //调用retry框架进行重试操作
            response = new guavaRetry().sendServiceWithRetry(request, rpcClient);
        }else {
            //只调用一次
            response= rpcClient.sendRequest(request);
        }
        //记录response的状态，上报给熔断器
        if (response.getCode() ==200){
            circuitBreaker.recordSuccess();
        }
        if (response.getCode()==500){
            circuitBreaker.recordFailure();
        }
        return response.getData();
    }

    /**
     * 获取代理对象
     * @param clazz
     * @param rpcReference
     * @return
     * @param <T>
     */
    public <T>T getProxy(Class<T> clazz,References rpcReference){
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        //记录接口服务与注解信息
        log.info("添加服务:{},注解信息:{}",clazz.getName(),rpcReference.toString());
        referencesHashMap.put(clazz.toString(),rpcReference);
        this.references = rpcReference;
        this.clazz = clazz;
        return (T)o;
    }
}
