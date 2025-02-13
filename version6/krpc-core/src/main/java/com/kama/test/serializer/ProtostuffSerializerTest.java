package com.kama.test.serializer;


import com.kama.pojo.User;
import common.exception.SerializeException;
import common.serializer.myserializer.ProtostuffSerializer;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @ClassName ProtostuffSerializerTest
 * @Description protostuff 序列化测试
 * @Author Tong
 * @LastChangeDate 2024-12-05 15:32
 * @Version v5.0
 */
public class ProtostuffSerializerTest {

    private ProtostuffSerializer serializer = new ProtostuffSerializer();

    @Test
    public void testSerializeAndDeserialize() {
        // 创建一个 User 对象
        User originalUser = User.builder()
                .id(1)
                .userName("TestUser")
                .gender(true)
                .build();

        // 序列化
        byte[] serialized = serializer.serialize(originalUser);
        assertNotNull("序列化结果不应为 null", serialized);

        // 反序列化
        Object deserialized = serializer.deserialize(serialized, 1);
        assertNotNull("反序列化结果不应为 null", deserialized);

        // 校验反序列化的对象是否与原对象相同
        assertTrue("反序列化的对象应该是 User 类型", deserialized instanceof User);
        User deserializedUser = (User) deserialized;
        assertEquals("反序列化的 User 应该与原 User 相同", originalUser, deserializedUser);
    }

    @Test
    public void testSerializeNullObject() {
        // 测试序列化 null 对象
        try {
            serializer.serialize(null);
            fail("序列化 null 对象时应抛出 IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot serialize null object", e.getMessage());
        }
    }

    @Test
    public void testDeserializeNullBytes() {
        // 测试反序列化 null 字节数组
        try {
            serializer.deserialize(null, 1);
            fail("反序列化 null 字节数组时应抛出 IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot deserialize null or empty byte array", e.getMessage());
        }
    }

    @Test
    public void testDeserializeEmptyBytes() {
        // 测试反序列化空字节数组
        try {
            serializer.deserialize(new byte[0], 1);
            fail("反序列化空字节数组时应抛出 IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot deserialize null or empty byte array", e.getMessage());
        }
    }

    @Test
    public void testDeserializeInvalidMessageType() {
        // 测试反序列化未知的 messageType
        byte[] serialized = serializer.serialize(new User(1, "TestUser", true));
        try {
            serializer.deserialize(serialized, 99); // 使用无效的 messageType
            fail("反序列化时应抛出 SerializeException");
        } catch (SerializeException e) {
            assertEquals("Unknown message type: 99", e.getMessage());
        }
    }
}