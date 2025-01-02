package com.thunder.eye.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.thunder.eye.entity.entity.JarDetailEntity;
import com.thunder.eye.entity.entity.ProgramEntity;
import com.thunder.eye.entity.entity.ding.DingMessage;
import com.thunder.eye.entity.entity.ding.DingTalkEntity;
import com.thunder.eye.service.DingNoticeService;
import com.thunder.eye.service.StateService;
import com.thunder.eye.utils.CacheUtil;
import com.thunder.eye.utils.ResponseEntity;
import com.xiaoleilu.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 钉钉通知服务实现类
 * 负责实现钉钉通知相关的功能
 *
 * @author ly
 * @date 2025年1月2日22:21:04
 */
@Service
@Slf4j
public class DingNoticeServiceImpl implements DingNoticeService {
    @Resource
    StateService stateService;
    @Resource
    CacheUtil cacheUtil;

    /**
     * 发送钉钉通知的方法
     *
     * @param dingMessage 钉钉消息对象，包含消息内容和 webhook 地址
     * @return 返回一个 ResponseEntity 对象，包含处理结果
     */
    @Override
    public ResponseEntity<String> notice(DingMessage dingMessage) {
        try {
            // 封装钉钉消息实体
            DingTalkEntity dingtalk = new DingTalkEntity(dingMessage);
            log.info("封装数据：{}", JSONObject.toJSONString(dingtalk));
            // 发送消息到钉钉
            String response = HttpUtil.post(dingMessage.getWebhook(), JSONObject.toJSONString(dingtalk));
            log.info("返回数据：{}", response);
            JSONObject jsonObject = JSONObject.parseObject(response);
            // 根据钉钉返回的消息判断是否发送成功
            if ("true".equals(jsonObject.getString("success"))) {
                return new ResponseEntity<String>().success("", "成功");
            } else {
                return new ResponseEntity<String>().fail("成功");
            }
        } catch (Exception e) {
            log.error("发送钉钉错误", e);
            return new ResponseEntity<String>().fail(e.getMessage());
        }
    }

    /**
     * 通知离线服务器的方法
     * 检查并通知所有离线的服务器，通过调用 notice 方法发送钉钉通知
     */
    @Override
    public void noticeDieServer() {
        List<JarDetailEntity> dieServer = stateService.getDieServer();
        if (dieServer != null && !dieServer.isEmpty()) {
            for (JarDetailEntity jarDetailEntity : dieServer) {
                ProgramEntity programEntity = cacheUtil.get(jarDetailEntity.getIp() + ":" + jarDetailEntity.getPort());
                String name = programEntity != null ? jarDetailEntity.getIp() : "未配置";
                DingMessage dingMessage = new DingMessage();
                dingMessage.setWebhook("");
                dingMessage.setValue("服务名称[" + name + "]\n服务器：" + jarDetailEntity.getIp()
                        + " \n端口：" + jarDetailEntity.getPort()
                        + " \n最后在线时间：" + jarDetailEntity.getLastTime() + "\n该服务疑似离线，请及时检查");
                notice(dingMessage);
            }
        }
    }


}
