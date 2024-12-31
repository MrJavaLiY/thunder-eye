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

    /**
     * 获取服务器状态信息
     *
     * 本方法通过调用缓存工具类获取当前所有缓存的数据，并将这些数据转换为ServerMesVo对象列表
     * 主要用于监控服务器状态，以便于及时发现和解决问题
     *
     * @return 服务器状态信息列表，每个元素包含服务器的IP和其上的Jar包详细信息
     */
    @Override
    public List<ServerMesVo> getServerState() {
        // 初始化服务器信息列表
        List<ServerMesVo> serverMesVos = new ArrayList<>();

        // 获取所有缓存数据，这些数据包含了服务器的状态信息
        Map<String, Object> data = heartbeatService.getCacheUtil().getAll();

        // 检查数据是否非空，以避免空指针异常
        if (data != null && !data.isEmpty()) {
            // 遍历缓存数据，将每个服务器的信息转换为ServerMesVo对象
            data.forEach((k, v) -> {
                // 将缓存中的值转换为JarDetailEntity列表，这里假设缓存中的值是List<JarDetailEntity>类型
                List<JarDetailEntity> jarDetailEntityList = (List<JarDetailEntity>) v;

                // 创建一个新的ServerMesVo对象来存储服务器信息
                ServerMesVo serverMesVo = new ServerMesVo();
                // 设置服务器的IP地址，这里假设键k是服务器的IP地址
                serverMesVo.setIp(k);
                // 设置服务器上的Jar包详细信息
                serverMesVo.setJarDetails(jarDetailEntityList);

                // 将填充好的服务器信息对象添加到列表中
                serverMesVos.add(serverMesVo);
            });
        }

        // 返回服务器信息列表
        return serverMesVos;
    }

    /**
     * 获取所有离线的服务器信息
     *
     * 本方法通过查询心跳服务缓存中的所有数据，筛选出离线的服务器信息
     * 主要逻辑包括：
     * 1. 获取缓存中的所有数据
     * 2. 遍历缓存数据，数据中包含多个JarDetailEntity对象
     * 3. 检查每个JarDetailEntity对象的运行状态，如果为离线状态，则添加到结果列表中
     *
     * @return 包含所有离线服务器信息的列表
     */
    @Override
    public List<JarDetailEntity> getDieServer() {
        // 初始化一个临时列表，用于存储离线的服务器信息
        List<JarDetailEntity> tempData = new ArrayList<>();
        // 从心跳服务缓存中获取所有数据
        Map<String, Object> data = heartbeatService.getCacheUtil().getAll();
        // 检查数据是否非空且不为空列表
        if (data != null && !data.isEmpty()) {
            // 遍历缓存中的每一项数据
            data.forEach((k, v) -> {
                // 将值转换为JarDetailEntity列表
                List<JarDetailEntity> jarDetailEntityList = (List<JarDetailEntity>) v;
                // 遍历JarDetailEntity列表
                for (JarDetailEntity jarDetailEntity : jarDetailEntityList) {
                    // 检查服务器状态是否为离线
                    if (jarDetailEntity.getRunStatus() == JarDetailEntity.OFFLINE) {
                        // 如果服务器离线，将其添加到临时列表中
                        tempData.add(jarDetailEntity);
                    }
                }
            });
        }
        // 返回包含所有离线服务器信息的列表
        return tempData;
    }
}
