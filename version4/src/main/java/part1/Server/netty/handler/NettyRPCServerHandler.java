package part1.Server.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import part1.Server.integration.References;
import part1.Server.provider.ServiceProvider;
import part1.Server.ratelimit.RateLimit;
import part1.common.Message.RpcRequest;
import part1.common.Message.RpcResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @version 1.0
 * @create 2024/2/26 16:40
 * 因为是服务器端，我们知道接受到请求格式是RPCRequest
 * Object类型也行，强制转型就行
 */
@AllArgsConstructor
@Slf4j
public class NettyRPCServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private ServiceProvider serviceProvider;

    //该方法会监听channel频道，获取来自服务端请求；然后调用getResponse方法返回服务响应结果
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        RpcResponse response = getResponse(request);
        ctx.writeAndFlush(response);
        ctx.close();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
    private RpcResponse getResponse(RpcRequest rpcRequest){
        log.info("服务端收到请求" + rpcRequest.toString());
        // 得到客户端接口，注解
        References references = rpcRequest.getReferences();
        String interfaceName = rpcRequest.getInterfaceName() + "." + references.version();
        // TODO 后续根据zookeeper配置中心信息 来决定其它调用配置（比如桶令牌的限流值）
        // 接口限流降级
        RateLimit rateLimit=serviceProvider.getRateLimitProvider().getRateLimit(interfaceName);
        if(!rateLimit.getToken()){
            //如果获取令牌失败，进行限流降级，快速返回结果
            log.info("服务限流！！");
            return RpcResponse.fail();
        }
        //得到服务端相应服务实现类
        Object service = serviceProvider.getService(interfaceName);
        log.info("查询的接口名称" + interfaceName);
        log.info("服务端相应服务实现类" + service.toString());
        //反射调用方法
        Method method=null;
        boolean isSuccess = false;
        Object invoke = null;
        try {
            method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamsType());
            invoke = method.invoke(service,rpcRequest.getParams());
            isSuccess = true;
            RpcResponse rpcResponse = new RpcResponse();
            rpcResponse.setCode(200);
            rpcResponse.setDataType(invoke.getClass());
            rpcResponse.setData(invoke);
            log.info("完成请求——请求返回结果（服务端返回）" + rpcResponse);
            return rpcResponse;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            log.info("方法执行错误");
            return RpcResponse.fail();
        } finally {
            // TODO 是否记录调用方信息待定（服务调用记录应该要保存到zooKeeper中）
            //serviceProvider.getRecordProvider().addRecord(interfaceName,recordInfo);
        }
    }
}