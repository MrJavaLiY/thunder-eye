package com.thunder.eye.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.thunder.eye.entity.entity.JarDetailEntity;
import com.thunder.eye.entity.entity.ding.DingMessage;
import com.thunder.eye.entity.entity.ding.DingTalkEntity;
import com.thunder.eye.service.DingNoticeService;
import com.thunder.eye.service.StateService;
import com.thunder.eye.utils.ResponseEntity;
import com.xiaoleilu.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liyang
 * @date 2024-01-06
 * @description:
 */
@Service
@Slf4j
public class DingNoticeServiceImpl implements DingNoticeService {
    @Resource
    StateService stateService;

    @Override
    public ResponseEntity<String> notice(DingMessage dingMessage) {
        try {
            DingTalkEntity dingtalk = new DingTalkEntity(dingMessage);
            log.info("封装数据：{}", JSONObject.toJSONString(dingtalk));
            String response = HttpUtil.post(dingMessage.getWebhook(), JSONObject.toJSONString(dingtalk));
            log.info("返回数据：{}", response);
            JSONObject jsonObject = JSONObject.parseObject(response);
            if ("true".equals(jsonObject.getString("success"))) {
                return new ResponseEntity<String>().success("", "成功");
            } else {
                return new ResponseEntity<String>().fail("成功");
            }
        } catch (Exception e) {
            log.error("发送钉钉错误{}", e);
            return new ResponseEntity<String>().fail(e.getMessage());
        }
    }

    @Override
    public void noticeDieServer() {
        List<JarDetailEntity> dieServer = stateService.getDieServer();
        if (dieServer != null && !dieServer.isEmpty()) {
            for (JarDetailEntity jarDetailEntity : dieServer) {
                DingMessage dingMessage = new DingMessage();
                dingMessage.setWebhook("");
                dingMessage.setValue("服务器：" + jarDetailEntity.getIp()
                        + " 端口：" + jarDetailEntity.getPort()
                        + " 最后在线时间：" + jarDetailEntity.getLastTime() + ";该服务疑似离线，请及时检查");
                notice(dingMessage);
            }
        }
    }


}
