package com.thunder.eye.service;


import com.thunder.eye.entity.entity.ding.DingMessage;
import com.thunder.eye.utils.ResponseEntity;

/**
 * @author liyang
 * @date 2024-01-06
 * @description:
 */
public interface DingNoticeService {
    public ResponseEntity<String> notice(DingMessage dingMessage) ;

    void noticeDieServer() ;
}
