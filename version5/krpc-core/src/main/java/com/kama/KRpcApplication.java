package com.kama;


import com.kama.config.KRpcConfig;
import com.kama.config.RpcConstant;
import common.util.ConfigUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName RpcApplication
 * @Description 测试配置顶，学习更多参考Dubbo
 * @Author Tong
 * @LastChangeDate 2024-12-05 11:22
 * @Version v5.0
 */
@Slf4j
public class KRpcApplication {
    private static volatile KRpcConfig rpcConfigInstance;

    public static void initialize(KRpcConfig customRpcConfig) {
        rpcConfigInstance = customRpcConfig;
        log.info("RPC 框架初始化，配置 = {}", customRpcConfig);
    }

    public static void initialize() {
        KRpcConfig customRpcConfig;
        try {
            customRpcConfig = ConfigUtil.loadConfig(KRpcConfig.class, RpcConstant.CONFIG_FILE_PREFIX);
            log.info("成功加载配置文件，配置文件名称 = {}", RpcConstant.CONFIG_FILE_PREFIX); // 添加成功加载的日志
        } catch (Exception e) {
            // 配置加载失败，使用默认配置
            customRpcConfig = new KRpcConfig();
            log.warn("配置加载失败，使用默认配置");
        }
        initialize(customRpcConfig);
    }

    public static KRpcConfig getRpcConfig() {
        if (rpcConfigInstance == null) {
            synchronized (KRpcApplication.class) {
                if (rpcConfigInstance == null) {
                    initialize();  // 确保在第一次调用时初始化
                }
            }
        }
        return rpcConfigInstance;
    }
}
