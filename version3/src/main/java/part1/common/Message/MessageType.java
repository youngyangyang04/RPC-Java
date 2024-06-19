package part1.common.Message;

import lombok.AllArgsConstructor;

/**
 * @author wxx
 * @version 1.0
 * @create 2024/6/2 22:29
 */
@AllArgsConstructor
public enum MessageType {
    REQUEST(0),RESPONSE(1);
    private int code;
    public int getCode(){
        return code;
    }
}