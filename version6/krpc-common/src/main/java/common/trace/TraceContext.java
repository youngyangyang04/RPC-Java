package common.trace;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Map;

/**
 * @author wxx
 * @version 1.0
 * @create 2025/2/18 18:37
 */
@Slf4j
public class TraceContext {

    public static void setTraceId(String traceId) {
        MDC.put("traceId",traceId);
    }

    public static String getTraceId() {
        return MDC.get("traceId");
    }

    public static void setSpanId(String spanId) {
        MDC.put("spanId",spanId);
    }

    public static String getSpanId() {
        return MDC.get("spanId");
    }
    public static void setParentSpanId(String parentSpanId) {
        MDC.put("parentSpanId",parentSpanId);
    }

    public static String getParentSpanId() {
        return MDC.get("parentSpanId");
    }
    public static void setStartTimestamp(String startTimestamp) {
        MDC.put("startTimestamp",startTimestamp);
    }

    public static String getStartTimestamp() {
        return MDC.get("startTimestamp");
    }
    public static Map<String,String> getCopy(){
        return MDC.getCopyOfContextMap();
    }
    public static void clone(Map<String,String> context){
        for(Map.Entry<String,String> entry:context.entrySet()){
            MDC.put(entry.getKey(),entry.getValue());
        }
    }
    public static void clear() {
        MDC.clear();
    }
}