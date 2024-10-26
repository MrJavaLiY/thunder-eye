package com.thunder.eye.entity.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DataValue extends ExcelEntity {
    @ExcelProperty(value = "ip")
    private String ip;
    @ExcelProperty(value = "port")
    private String port;
    @ExcelProperty(value = "jarName")
    private String jarName;
    @ExcelProperty(value = "jarPath")
    private String jarPath;
    @ExcelProperty(value = "gcMessage")
    private String gcMessage;
    @ExcelProperty(value = "survivalTime")
    private Date survivalTime;
    @ExcelProperty(value = "status")
    private Boolean status;
    @ExcelProperty(value = "runStat")
    private Boolean runStat;
}
