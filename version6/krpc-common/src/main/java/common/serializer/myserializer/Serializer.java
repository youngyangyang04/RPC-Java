package common.serializer.myserializer;




import java.util.HashMap;
import java.util.Map;

/**
 * @InterfaceName Serializer
 * @Description 序列化接口
 * @Author Tong
 * @LastChangeDate 2024-11-29 10:33
 * @Version v5.0
 */
public interface Serializer {
    byte[] serialize(Object obj);

    Object deserialize(byte[] bytes, int messageType);

    int getType();

    // 定义静态常量 serializerMap
    static final Map<Integer, Serializer> serializerMap = new HashMap<>();

    // 使用 Map 存储序列化器
    static Serializer getSerializerByCode(int code) {
        // 静态映射，保证只初始化一次
        if(serializerMap.isEmpty()) {
            serializerMap.put(0, new ObjectSerializer());
            serializerMap.put(1, new JsonSerializer());
            serializerMap.put(2, new KryoSerializer());
            serializerMap.put(3, new HessianSerializer());
            serializerMap.put(4, new ProtostuffSerializer());
        }
        return serializerMap.get(code); // 如果不存在，则返回 null
    }
}
