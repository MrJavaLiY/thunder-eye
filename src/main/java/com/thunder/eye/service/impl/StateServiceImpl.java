package com.thunder.eye.service.impl;

import com.thunder.eye.entity.entity.JarDetailEntity;
import com.thunder.eye.entity.vo.ServerMesVo;
import com.thunder.eye.service.HeartbeatService;
import com.thunder.eye.service.StateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StateServiceImpl implements StateService {
    @Resource
    HeartbeatService heartbeatService;

    @Override
    public List<ServerMesVo> getServerState() {
        List<ServerMesVo> serverMesVos = new ArrayList<>();
        Map<String, Object> data = heartbeatService.getCacheUtil().getAll();
        if (data != null && !data.isEmpty()) {
            data.forEach((k, v) -> {
                List<JarDetailEntity> jarDetailEntityList = (List<JarDetailEntity>) v;
                ServerMesVo serverMesVo = new ServerMesVo();
                serverMesVo.setIp(k);
                serverMesVo.setJarDetails(jarDetailEntityList);
                serverMesVos.add(serverMesVo);
            });
        }
        return serverMesVos;
    }

    @Override
    public List<JarDetailEntity> getDieServer() {
        List<JarDetailEntity> tempData = new ArrayList<>();
        Map<String, Object> data = heartbeatService.getCacheUtil().getAll();
        if (data != null && !data.isEmpty()) {
            data.forEach((k, v) -> {
                List<JarDetailEntity> jarDetailEntityList = (List<JarDetailEntity>) v;
                for (JarDetailEntity jarDetailEntity : jarDetailEntityList) {
                    if (jarDetailEntity.getRunStatus() == JarDetailEntity.OFFLINE) {
                        tempData.add(jarDetailEntity);
                    }
                }
            });
        }
        return tempData;
    }
}
