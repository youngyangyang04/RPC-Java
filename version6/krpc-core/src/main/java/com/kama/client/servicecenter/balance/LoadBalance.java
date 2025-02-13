package com.kama.client.servicecenter.balance;


import java.util.List;

/**
 * @InterfaceName LoadBalance
 * @Description 负载均衡接口
 * @Author Tong
 * @LastChangeDate 2024-12-02 10:40
 * @Version v5.0
 */

public interface LoadBalance {
    String balance(List<String> addressList);

    void addNode(String node);

    void delNode(String node);
}
