package part1.Server.record.impl;

import lombok.Data;
import part1.Server.record.ServiceRecord;
import part1.common.pojo.RecordInfo;

import java.util.ArrayList;
import java.util.List;

@Data
public class ServiceRecordImpl implements ServiceRecord {

    List<RecordInfo> recordInfo;

    public ServiceRecordImpl() {
        this.recordInfo = new ArrayList<>();
    }

    @Override
    public Integer getTimes(String serviceName) {
        return recordInfo.size();
    }

    @Override
    public List<RecordInfo>  getRecord() {
        return recordInfo;
    }

    public void addRecord(RecordInfo recordInfo){
        this.recordInfo.add(recordInfo);
    }
}
