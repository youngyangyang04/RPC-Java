package part1.Client;


import part1.Client.proxy.ClientProxy;


public class TestClient {
    public static void main(String[] args) throws InterruptedException {
        ClientProxy clientProxy = new ClientProxy();
        //获取服务对象
//        UserService proxy=clientProxy.getProxy(UserService.class);
//        for(int i = 0; i < 5; i++) {
//            Integer i1 = i;
//            if (i%30==0) {
//                Thread.sleep(1000*2);
//            }
//            new Thread(()->{
//                try{
//                    User user = proxy.getUserByUserId(i1);
//                    System.out.println("从服务端得到的user="+user.toString());
//
//                    Integer id = proxy.insertUserId(User.builder().id(i1).userName("User" + i1.toString()).sex(true).build());
//                    System.out.println("向服务端插入user的id"+id);
//                } catch (NullPointerException e){
//                    System.out.println("user为空");
//                    e.printStackTrace();
//                }
//            }).start();
//        }
    }
}