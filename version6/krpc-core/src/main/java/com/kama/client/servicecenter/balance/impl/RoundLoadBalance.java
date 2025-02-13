package com.kama.client.servicecenter.balance.impl;

import com.kama.client.servicecenter.balance.LoadBalance;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName RoundLoadBalance
 * @Description 轮询法
 * @Author Tong
 * @LastChangeDate 2024-12-02 10:41
 * @Version v5.0
 */
@Slf4j
public class RoundLoadBalance implements LoadBalance {

    // 使用 AtomicInteger 保证线程安全
    private AtomicInteger choose = new AtomicInteger(0);

    private List<String> addressList = new CopyOnWriteArrayList<>();

    @Override
    public String balance(List<String> addressList) {
        if (addressList == null || addressList.isEmpty()) {
            throw new IllegalArgumentException("Address list cannot be null or empty");
        }

        // 获取当前索引并更新为下一个
        int currentChoose = choose.getAndUpdate(i -> (i + 1) % addressList.size());

        String selectedServer = addressList.get(currentChoose);
        log.info("负载均衡选择了服务器: {}", selectedServer);
        return selectedServer;  // 返回被选择的服务器地址
    }

    @Override
    public void addNode(String node) {
        // 如果是动态添加节点，可以将节点加入到 addressList 中
        addressList.add(node);
        log.info("节点 {} 已加入负载均衡", node);
    }

    @Override
    public void delNode(String node) {
        // 如果是动态删除节点，可以将节点从 addressList 中移除
        addressList.remove(node);
        log.info("节点 {} 已从负载均衡中移除", node);
    }
}
