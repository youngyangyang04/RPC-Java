package part2.Client.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wxx
 * @version 1.0
 * @create 2024/6/4 0:57
 */
public class serviceCache {
    //key: serviceName 服务名
    //value： addressList 服务提供者列表
    private static Map<String, List<String>> cache=new HashMap<>();

    //添加服务
    public void addServcieToCache(String serviceName,String address){
        if(cache.containsKey(serviceName)){
            List<String> addressList = cache.get(serviceName);
            addressList.add(address);
            System.out.println("将name为"+serviceName+"和地址为"+address+"的服务添加到本地缓存中");
        }else {
            List<String> addressList=new ArrayList<>();
            addressList.add(address);
            cache.put(serviceName,addressList);
        }
    }
    //修改服务地址
    public void replaceServiceAddress(String serviceName,String oldAddress,String newAddress){
        if(cache.containsKey(serviceName)){
            List<String> addressList=cache.get(serviceName);
            addressList.remove(oldAddress);
            addressList.add(newAddress);
        }else {
            System.out.println("修改失败，服务不存在");
        }
    }
    //从缓存中取服务地址列表
    public List<String> getServiceListFromCache(String serviceName){
        if(!cache.containsKey(serviceName)) {
            return null;
        }
        return cache.get(serviceName);
    }

    //从缓存中删除服务地址
    public void delete(String serviceName,String address){
        List<String> addressList = cache.get(serviceName);
        addressList.remove(address);
        System.out.println("将name为"+serviceName+"和地址为"+address+"的服务从本地缓存中删除");
    }
}
