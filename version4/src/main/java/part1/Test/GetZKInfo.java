package part1.Test;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

public class GetZKInfo {
    private static final String CONNECT_STRING = "localhost:2181"; // ZooKeeper服务器地址
    private static final int SESSION_TIMEOUT = 5000; // 会话超时时间

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper = new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                // 处理事件
            }
        });
        String configNode = "/"; // 配置节点路径
        try {
            // 获取配置节点下所有数据
            List<String> children = zooKeeper.getChildren(configNode, false);
            for (String child : children) {
                String childNode = configNode + child;
                List<String> kids = zooKeeper.getChildren(childNode, false);
                for (String kid : kids) {
                    Stat stat = zooKeeper.exists(childNode + "/" + kid, false);
                    if (stat != null) {
                        byte[] data = zooKeeper.getData(childNode + "/" + kid, false, stat);
                        System.out.println("Node: " + childNode + "/" + kid + ", Data: " + new String(data));
                        List<String> info = zooKeeper.getChildren(childNode + "/" + kid, false);
                        try {
                            System.out.println(info.get(0));
                        } catch (Exception e) {

                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        zooKeeper.close();
    }
}
