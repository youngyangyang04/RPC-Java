package part1.Client;


import part1.Client.proxy.ClientProxy;
import part1.common.pojo.User;
import part1.common.service.UserService;


/**
 * @author wxx
 * @version 1.0
 * @create 2024/2/6 18:39
 */

public class TestClient {
    public static void main(String[] args) throws InterruptedException {
        ClientProxy clientProxy=new ClientProxy();
        //ClientProxy clientProxy=new proxy.Client.part1.ClientProxy("127.0.0.1",9999,0);
        UserService proxy=clientProxy.getProxy(UserService.class);

        User user = proxy.getUserByUserId(1);
        System.out.println("从服务端得到的user="+user.toString());

        User u=User.builder().id(100).userName("wxx").sex(true).build();
        Integer id = proxy.insertUserId(u);
        System.out.println("向服务端插入user的id"+id);
    }
}
