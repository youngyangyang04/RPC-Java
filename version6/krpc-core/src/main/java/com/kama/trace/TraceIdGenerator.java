package com.kama.trace;

import java.util.UUID;

/**
 * @author wxx
 * @version 1.0
 * @create 2025/2/16 23:05
 */
public class TraceIdGenerator {
    //机器序列号默认为0，真实场景中从配置中心获取
    private static final SnowflakeIdGenerator SNOWFLAKE =new SnowflakeIdGenerator(0L);

    public static  String generateTraceId() {
        return Long.toHexString(SNOWFLAKE.nextId());
    }
    public static String generateTraceIdUUID(){
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        // 去掉连字符
        String uuidWithoutHyphens = uuidString.replace("-", "");
        return uuidWithoutHyphens;
    }
    public static String generateSpanId() {
        return String.valueOf(System.currentTimeMillis());
    }
    static class SnowflakeIdGenerator {
        // 机器 ID（0~1023）
        private final long workerId;

        // 基准时间（2021-01-01 00:00:00）
        private final long epoch = 1609459200000L;

        // 序列号（0~4095）
        private long sequence = 0L;

        // 上一次生成 ID 的时间戳
        private long lastTimestamp = -1L;

        // 构造函数，传入机器 ID
        public SnowflakeIdGenerator(long workerId) {
            if (workerId < 0 || workerId > 1023) {
                throw new IllegalArgumentException("Worker ID 必须在 0~1023 之间");
            }
            this.workerId = workerId;
        }

        // 生成下一个 ID
        public synchronized long nextId() {
            long timestamp = System.currentTimeMillis();

            // 如果当前时间小于上一次生成 ID 的时间，说明时钟回拨
            if (timestamp < lastTimestamp) {
                throw new RuntimeException("时钟回拨！");
            }

            // 如果当前时间等于上一次生成 ID 的时间，递增序列号
            if (timestamp == lastTimestamp) {
                sequence = (sequence + 1) & 0xFFF; // 12 位序列号，最大 4095
                if (sequence == 0) {
                    // 如果序列号溢出，等待下一毫秒
                    timestamp = waitNextMillis(lastTimestamp);
                }
            } else {
                // 如果当前时间大于上一次生成 ID 的时间，重置序列号
                sequence = 0L;
            }

            // 更新上一次生成 ID 的时间戳
            lastTimestamp = timestamp;

            // 生成 ID
            return ((timestamp - epoch) << 22) | (workerId << 12) | sequence;
        }

        // 等待下一毫秒
        private long waitNextMillis(long lastTimestamp) {
            long timestamp = System.currentTimeMillis();
            while (timestamp <= lastTimestamp) {
                timestamp = System.currentTimeMillis();
            }
            return timestamp;
        }
    }
}
