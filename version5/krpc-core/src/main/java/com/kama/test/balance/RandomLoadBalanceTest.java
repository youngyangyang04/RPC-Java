package com.kama.test.balance;

import com.kama.client.servicecenter.balance.impl.RandomLoadBalance;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @ClassName RandomLoadBalanceTest
 * @Description 随机负载均衡器测试
 * @Author Tong
 * @LastChangeDate 2024-12-05 15:43
 * @Version v5.0
 */
public class RandomLoadBalanceTest {

    private RandomLoadBalance loadBalance;

    @Before
    public void setUp() {
        // 在每个测试前初始化负载均衡器
        loadBalance = new RandomLoadBalance();
    }

    @Test
    public void testBalance_WithNonEmptyList() {
        // 准备一个非空的地址列表
        List<String> addressList = Arrays.asList("server1", "server2", "server3");

        // 使用 balance 方法选择一个服务器
        String selectedServer = loadBalance.balance(addressList);

        // 确保选择的服务器在列表中
        assertTrue(addressList.contains(selectedServer));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBalance_WithEmptyList() {
        // 测试空的节点列表，应该抛出 IllegalArgumentException 异常
        loadBalance.balance(Arrays.asList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBalance_WithNullList() {
        // 测试 null 的节点列表，应该抛出 IllegalArgumentException 异常
        loadBalance.balance(null);
    }

    @Test
    public void testAddNode() {
        // 测试添加节点到负载均衡器
        loadBalance.addNode("server4");

        // 确保新添加的节点在负载均衡器中
        List<String> addressList = Arrays.asList("server1", "server2", "server3", "server4");
        String selectedServer = loadBalance.balance(addressList);
        assertTrue(addressList.contains(selectedServer));
    }

    @Test
    public void testDelNode() {
        // 测试从负载均衡器中移除节点
        loadBalance.addNode("server4");
        loadBalance.delNode("server4");

        // 确保删除后的节点不再在负载均衡器中
        List<String> addressList = Arrays.asList("server1", "server2", "server3");
        String selectedServer = loadBalance.balance(addressList);
        assertFalse(addressList.contains("server4"));
    }
}
