package com.thunder.eye.dataBase.dataBase;

import com.thunder.eye.entity.entity.excel.SchedulerLog;
import org.springframework.stereotype.Component;

@Component
public class SchedulerExcelDataBase extends ExcelDataBase<SchedulerLog>  {

    public SchedulerExcelDataBase() {
        super();
        clazz=SchedulerLog.class;
        path=SCHEDULER_PATH;
        sheetName=SCHEDULER_SHEET_NAME;
    }


    @Override
    String getIndex(SchedulerLog schedulerLog) {
        return schedulerLog.getIndex();
    }
}

