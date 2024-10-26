package com.tunder.eye.condition.vo;

import com.thunder.eye.entity.entity.excel.DataValue;
import lombok.Data;

import java.util.List;

@Data
public class DataValueVO {

    private int page;
    private int size;
    private int total;
    private List<DataValue> data;
}
