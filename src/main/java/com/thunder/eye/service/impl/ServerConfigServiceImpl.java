package com.thunder.eye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.thunder.eye.condition.RequestCondition;
import com.thunder.eye.dao.ServerMapper;
import com.thunder.eye.entity.sql.ServerConfig;
import com.thunder.eye.service.ServerConfigService;
import com.thunder.eye.utils.ResponseEntity;
import com.xiaoleilu.hutool.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
@Slf4j
public class ServerConfigServiceImpl implements ServerConfigService {
    @Resource
    ServerMapper serverMapper;

    @Override
    public ResponseEntity<?> add(RequestCondition condition) {
        QueryWrapper<ServerConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("ip", condition.getIp());
        Long count = serverMapper.selectCount(wrapper);
        if (count > 0) {
            return new ResponseEntity<>()
                    .fail("服务器已存在");
        }

        ServerConfig serverConfig = ServerConfig.builder()
                .ip(condition.getIp())
                .serverType(condition.getServerType())
                .name(condition.getName())
                .username(condition.getUser())
                .password(condition.getPassword())
                .enable(false)
                .dtime(DateUtil.date())
                .build();
        int i = serverMapper.insert(serverConfig);
        return new ResponseEntity<>().success(i, "添加成功");
    }

    @Override
    public ResponseEntity<?> del(RequestCondition condition) {
        if (condition.getIndex() == null || "".equals(condition.getIndex())) {
            return new ResponseEntity<>().fail("参数错误");
        }

        return new ResponseEntity<>().success(serverMapper.deleteById(condition.getIndex()), "删除成功");
    }

    @Override
    public ResponseEntity<?> update(RequestCondition condition) {
        if (condition.getIndex() == null || "".equals(condition.getIndex())) {
            return new ResponseEntity<>().fail("参数错误");
        }
        ServerConfig.ServerConfigBuilder serverConfig = ServerConfig.builder();
        serverConfig.id(Long.valueOf(condition.getIndex()));
        if (StringUtils.hasText(condition.getIp() )) {
            serverConfig.ip(condition.getIp());
        }
        if (StringUtils.hasText(condition.getServerType())) {
            serverConfig.serverType(condition.getServerType());
        }
        if (StringUtils.hasText(condition.getName())) {
            serverConfig.name(condition.getName());
        }
        if (StringUtils.hasText(condition.getUser())) {
            serverConfig.username(condition.getUser());
        }
        if (StringUtils.hasText(condition.getPassword() )) {
            serverConfig.password(condition.getPassword());
        }
        if (condition.getEnable() != null) {
            serverConfig.enable(condition.getEnable());
        }
        ServerConfig serverConfigB = serverConfig.dtime(DateUtil.date()).build();
        int i = serverMapper.updateById(serverConfigB);
        return new ResponseEntity<>().success(i, "修改成功");
    }

    @Override
    public ResponseEntity<?> getOne(RequestCondition condition) {
        QueryWrapper<ServerConfig> wrapper = new QueryWrapper<>();
        if (condition.getIndex() != null && !"".equals(condition.getIndex())) {
            wrapper.eq("id", condition.getIndex());
        }
        if (condition.getIp() != null && !"".equals(condition.getIp())) {
            wrapper.eq("ip", condition.getIp());
        }
        if (condition.getUser() != null && !"".equals(condition.getUser())) {
            wrapper.eq("username", condition.getUser());
        }
        ServerConfig serverConfig = serverMapper.selectOne(wrapper);
        return new ResponseEntity<>().success(serverConfig, "查询成功");
    }


    @Override
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>().success(serverMapper.selectList(null), "查询成功");
    }

    @Override
    public ResponseEntity<?> getPage(RequestCondition condition) {
        QueryWrapper<ServerConfig> wrapper = new QueryWrapper<>();
        return null;
    }

    @Override
    public ResponseEntity<?> getAllEnable() {
        QueryWrapper<ServerConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("enable", true);
        return new ResponseEntity<>().success(serverMapper.selectList(wrapper), "查询成功");
    }
}
