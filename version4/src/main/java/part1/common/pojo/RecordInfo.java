package part1.common.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
/**
 * 以单条调用记录为维度
 */
public class RecordInfo {
    //服务名称
    String ServiceName;
    //调用者名称
    String name;
    //是否成功
    boolean success;
    //请求结果
    Object response;
    //调用时间
    LocalDateTime time;
}
