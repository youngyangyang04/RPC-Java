package part1.common.Message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import part1.Server.integration.References;

import java.io.Serializable;

/**
 * @author wxx
 * @version 1.0
 * @create 2024/2/1 18:30
 * 定义发送的消息格式
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RpcRequest implements Serializable {
    //服务类名，客户端只知道接口
    private String interfaceName;
    //客户端接口注解
    private References references;
    //调用的方法名
    private String methodName;
    //参数列表
    private Object[] params;
    //参数类型
    private Class<?>[] paramsType;
}
