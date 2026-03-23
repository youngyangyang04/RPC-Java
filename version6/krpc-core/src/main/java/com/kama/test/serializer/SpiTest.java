package com.kama.test.serializer;

import common.serializer.myserializer.*;
import common.spi.SpiLoader;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @ClassName SpiTest
 * @Description spi测试
 * @Author Kano07
 * @LastChangeDate 2026-3-23 14:02
 * @Version v6.0
 */
public class SpiTest {
    @Test
    public void testSpi() {
        SpiLoader.loadSpi(Serializer.class);
        Serializer serializer = (Serializer) SpiLoader.getInstance(Serializer.class, "kryo");
        assertTrue("SPI 获取的实例应该是 KryoSerializer 类型", serializer instanceof KryoSerializer);
        serializer = (Serializer) SpiLoader.getInstance(Serializer.class, "protobuf");
        assertTrue("SPI 获取的实例应该是 ProtostuffSerializer 类型", serializer instanceof ProtostuffSerializer);
        serializer = (Serializer) SpiLoader.getInstance(Serializer.class, "json");
        assertTrue("SPI 获取的实例应该是 JsonSerializer 类型", serializer instanceof JsonSerializer);
        serializer = (Serializer) SpiLoader.getInstance(Serializer.class, "Hessian");
        assertTrue("SPI 获取的实例应该是 HessianSerializer 类型", serializer instanceof HessianSerializer);
        serializer = (Serializer) SpiLoader.getInstance(Serializer.class, "jdk");
        assertTrue("SPI 获取的实例应该是 ObjectSerializer 类型", serializer instanceof ObjectSerializer);
    }
}
