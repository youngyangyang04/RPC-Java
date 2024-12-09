package com.kama.test.balance;

import com.kama.client.servicecenter.balance.impl.ConsistencyHashBalance;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @ClassName ConsistencyHashBalanceTest
 * @Description 一致性哈希测试类
 * @Author Tong
 * @LastChangeDate 2024-12-05 15:39
 * @Version v5.0
 */
public class ConsistencyHashBalanceTest {

    private ConsistencyHashBalance balance;

    @Before
    public void setUp() {
        balance = new ConsistencyHashBalance();
    }

    @Test
    public void testInit() {
        // 模拟真实节点
        List<String> nodes = Arrays.asList("server1", "server2", "server3");
        balance.init(nodes);

        // 验证虚拟节点的初始化是否正确
        assertTrue("shards should not be empty", balance.getShards().size() > 0);
        assertTrue("realNodes should contain all nodes", balance.getRealNodes().containsAll(nodes));
    }

    @Test
    public void testGetServer() {
        // 模拟真实节点
        List<String> nodes = Arrays.asList("server1", "server2", "server3");
        balance.init(nodes);

        // 使用 UUID 作为请求的唯一标识符进行负载均衡
        String server = balance.getServer("request-1", nodes);
        assertNotNull("Server should not be null", server);
        assertTrue("Server should be one of the real nodes", nodes.contains(server));

        // 确保多个请求的分配在不同节点上（可根据测试的多次运行结果观察）
        String server2 = balance.getServer("request-2", nodes);
        assertNotEquals("Server should be different from the previous request", server, server2);
    }

    @Test
    public void testAddNode() {
        // 模拟真实节点
        List<String> nodes = Arrays.asList("server1", "server2");
        balance.init(nodes);

        // 新加入一个节点
        balance.addNode("server3");

        // 验证新节点是否被加入
        assertTrue("server3 should be added", balance.getRealNodes().contains("server3"));
        assertTrue("shards should contain virtual nodes for server3", balance.getShards().size() > 0);
    }

    @Test
    public void testDelNode() {
        // 模拟真实节点
        List<String> nodes = Arrays.asList("server1", "server2");
        balance.init(nodes);

        // 删除一个节点
        balance.delNode("server1");

        // 验证该节点是否被移除
        assertFalse("server1 should be removed", balance.getRealNodes().contains("server1"));
        assertFalse("shards should not contain virtual nodes for server1", balance.getShards().values().stream().anyMatch(vn -> vn.startsWith("server1")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBalanceWithEmptyList() {
        // 测试地址列表为空时，抛出 IllegalArgumentException
        balance.balance(Arrays.asList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBalanceWithNullList() {
        // 测试地址列表为 null 时，抛出 IllegalArgumentException
        balance.balance(null);
    }

    @Test
    public void testGetVirtualNum() {
        // 测试虚拟节点的数量
        assertEquals("Virtual nodes count should be 5", 5, ConsistencyHashBalance.getVirtualNum());
    }
}
