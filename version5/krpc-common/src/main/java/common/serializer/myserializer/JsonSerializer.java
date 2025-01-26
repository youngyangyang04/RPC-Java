package common.serializer.myserializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.message.RpcRequest;
import common.message.RpcResponse;


/**
 * @ClassName JsonSerializer
 * @Description json序列化
 * @Author Tong
 * @LastChangeDate 2024-11-29 10:33
 * @Version v5.0
 */
public class JsonSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        byte[] bytes = JSONObject.toJSONBytes(obj);
        return bytes;
    }

    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Object obj = null;
        // 传输的消息分为request与response
        switch (messageType){
            case 0:
                RpcRequest request = JSON.parseObject(bytes, RpcRequest.class);
                Object[] objects = new Object[request.getParams().length];
                // 把json字串转化成对应的对象， fastjson可以读出基本数据类型，不用转化
                // 对转换后的request中的params属性逐个进行类型判断
                for(int i = 0; i < objects.length; i++){
                    Class<?> paramsType = request.getParamsType()[i];
                    //判断每个对象类型是否和paramsTypes中的一致
                    if (!paramsType.isAssignableFrom(request.getParams()[i].getClass())){
                        //如果不一致，就行进行类型转换
                        objects[i] = JSONObject.toJavaObject((JSONObject) request.getParams()[i],request.getParamsType()[i]);
                    }else{
                        //如果一致就直接赋给objects[i]
                        objects[i] = request.getParams()[i];
                    }
                }
                request.setParams(objects);
                obj = request;
                break;
            case 1:
                RpcResponse response = JSON.parseObject(bytes, RpcResponse.class);
                // 如果类型为空，说明返回错误
                if(response.getDataType()==null){
                    obj = RpcResponse.fail("类型为空");
                    break;
                }
                Class<?> dataType = response.getDataType();
                //判断转化后的response对象中的data的类型是否正确
                if(response.getData() != null && !dataType.isAssignableFrom(response.getData().getClass())){
                    response.setData(JSONObject.toJavaObject((JSONObject) response.getData(),dataType));
                }
                obj = response;
                break;
            default:
                System.out.println("暂时不支持此种消息");
                throw new RuntimeException();
        }
        return obj;
    }

    //1 代表json序列化方式
    @Override
    public int getType() {
        return 1;
    }

    @Override
    public String toString() {
        return "Json";
    }
}
