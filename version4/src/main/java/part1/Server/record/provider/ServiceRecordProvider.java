package part1.Server.record.provider;

import lombok.extern.slf4j.Slf4j;
import part1.Server.record.impl.ServiceRecordImpl;
import part1.common.pojo.RecordInfo;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ServiceRecordProvider {
    private Map<String, ServiceRecordImpl> recordMap = new HashMap<>();

    public ServiceRecordImpl getRecord(String interfaceName){
        if(!recordMap.containsKey(interfaceName)){
            ServiceRecordImpl record = new ServiceRecordImpl();
            recordMap.put(interfaceName,record);
            return record;
        }
        return recordMap.get(interfaceName);
    }

    public void addRecord(String interfaceName, RecordInfo recordInfo){
        if (recordMap.containsKey(interfaceName)){
            recordMap.get(interfaceName).getRecordInfo().add(recordInfo);
            log.info(interfaceName + "添加记录" + recordInfo);
        }else {
            ServiceRecordImpl record = new ServiceRecordImpl();
            recordMap.put(interfaceName, record);
            log.info(interfaceName + "新建记录");
        }
    }
}
