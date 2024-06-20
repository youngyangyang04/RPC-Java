package part2.Client.serviceCenter.balance.impl;

import part2.Client.serviceCenter.balance.LoadBalance;

import java.util.List;
import java.util.Random;

/**
 * @author wxx
 * @version 1.0
 * @create 2024/6/19 21:20
 * 随机 负载均衡
 */
public class RandomLoadBalance implements LoadBalance {
    @Override
    public String balance(List<String> addressList) {
        Random random=new Random();
        int choose = random.nextInt(addressList.size());
        System.out.println("负载均衡选择了"+choose+"服务器");
        return null;
    }
    public void addNode(String node){} ;
    public void delNode(String node){};
}
