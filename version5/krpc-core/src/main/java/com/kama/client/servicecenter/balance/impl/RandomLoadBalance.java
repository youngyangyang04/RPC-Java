package com.kama.client.servicecenter.balance.impl;

import com.kama.client.servicecenter.balance.LoadBalance;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName RandomLoadBalance
 * @Description 随机法
 * @Author Tong
 * @LastChangeDate 2024-12-02 10:40
 * @Version v5.0
 */
@Slf4j
public class RandomLoadBalance implements LoadBalance {
    // 将Random声明为类级别的字段
    private final Random random = new Random();

    private final List<String> addressList = new CopyOnWriteArrayList<>();

    @Override
    public String balance(List<String> addressList) {
        if (addressList == null || addressList.isEmpty()) {
            throw new IllegalArgumentException("Address list cannot be null or empty");
        }

        int choose = random.nextInt(addressList.size());
        log.info("负载均衡选择了第 {} 号服务器，地址是：{}", choose, addressList.get(choose));
        return addressList.get(choose);  // 返回选择的服务器地址
    }

    @Override
    public void addNode(String node) {
        // 如果是动态添加节点，可以将节点加入到addressList中
        addressList.add(node);
        log.info("节点 {} 已加入负载均衡", node);
    }

    @Override
    public void delNode(String node) {
        // 如果是动态删除节点，可以将节点从addressList中移除
        addressList.remove(node);
        log.info("节点 {} 已从负载均衡中移除", node);
    }
}
