package com.thunder.eye.dataBase.dataBase;

import com.thunder.eye.entity.entity.excel.NoticeConfig;
import org.springframework.stereotype.Component;

@Component
public class NoticeExcelDataBase extends ExcelDataBase<NoticeConfig>  {

    public NoticeExcelDataBase() {
        super();
        clazz= NoticeConfig.class;
        path=NOTICE_PATH;
        sheetName=NOTICE_SHEET_NAME;
    }


    @Override
    String getIndex(NoticeConfig noticeConfig) {
        return noticeConfig.getIndex();
    }
}

