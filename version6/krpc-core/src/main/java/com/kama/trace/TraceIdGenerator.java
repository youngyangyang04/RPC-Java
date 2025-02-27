package com.kama.trace;

import java.util.UUID;

/**
 * @author wxx
 * @version 1.0
 * @create 2025/2/16 23:05
 */
public class TraceIdGenerator {
    public static  String generateTraceId() {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        // 去掉连字符
        String uuidWithoutHyphens = uuidString.replace("-", "");
        return uuidWithoutHyphens;
    }

    public static String generateSpanId() {
        return String.valueOf(System.currentTimeMillis());
    }
}
