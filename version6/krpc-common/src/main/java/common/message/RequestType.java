package common.message;

import lombok.AllArgsConstructor;

/**
 * @author wxx
 * @version 1.0
 * @create 2025/2/28 18:32
 */
@AllArgsConstructor
public enum RequestType {
    NORMAL(0), HEARTBEAT(1);
    private int code;

    public int getCode() {
        return code;
    }
}
