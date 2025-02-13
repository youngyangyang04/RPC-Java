package com.kama.consumer;


import com.kama.config.KRpcConfig;
import common.util.ConfigUtil;

/**
 * @ClassName ConsumerTestConfig
 * @Description 测试配置顶
 * @Author Tong
 * @LastChangeDate 2024-12-05 11:29
 * @Version v1.0
 */
public class ConsumerTestConfig {
    public static void main(String[] args) {
        KRpcConfig rpc = ConfigUtil.loadConfig(KRpcConfig.class, "rpc");
        System.out.println(rpc);
    }

}
