package part3.Client;


import lombok.extern.slf4j.Slf4j;
import part3.common.pojo.User;
import part3.common.service.UserService;
import part3.Client.proxy.ClientProxy;


/**
 * @author wxx
 * @version 1.0
 * @create 2024/2/6 18:39
 */

public class TestClient {
    public static void main(String[] args) {
        ClientProxy clientProxy=new ClientProxy();
        //ClientProxy clientProxy=new part2.Client.proxy.ClientProxy("127.0.0.1",9999,0);
        UserService proxy=clientProxy.getProxy(UserService.class);

        User user = proxy.getUserByUserId(1);
        System.out.println("从服务端得到的user="+user.toString());

        User u=User.builder().id(100).userName("wxx").sex(true).build();
        Integer id = proxy.insertUserId(u);
        System.out.println("向服务端插入user的id"+id);
    }
}
