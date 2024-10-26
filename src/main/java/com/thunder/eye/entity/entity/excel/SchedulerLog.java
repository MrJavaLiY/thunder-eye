package com.thunder.eye.entity.entity.excel;

import lombok.Data;

@Data
public class SchedulerLog  extends ExcelEntity{
    private String ThreadName;
    private boolean status;
    private Data time;
    private String value;
}
