package com.thunder.eye.service;

import com.thunder.eye.condition.PageRequestCondition;
import com.thunder.eye.condition.RequestCondition;
import com.thunder.eye.condition.vo.DataValueVO;
import com.thunder.eye.entity.entity.excel.DataValue;
import com.thunder.eye.utils.ResponseEntity;

import java.util.List;

/**
 * 持久化服务信息
 */
public interface DataPerService {
    ResponseEntity<String> add(DataValue dataValue);

    ResponseEntity<DataValueVO> getPageData(PageRequestCondition condition);

    ResponseEntity<DataValue> getOne(RequestCondition condition);

    ResponseEntity<String> updateData(DataValue dataValue);

    ResponseEntity<String> deleteData(DataValue dataValue);


    void reNewPer(List<DataValue> dataValue);
}
