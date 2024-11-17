package part1.Server.record;

import part1.common.pojo.RecordInfo;

import java.util.List;

public interface ServiceRecord {

    //调用总次数
    Integer getTimes(String serviceName);
    //调用者的调用信息
    List<RecordInfo>  getRecord();
    //添加方法
    void addRecord(RecordInfo recordInfo);

}