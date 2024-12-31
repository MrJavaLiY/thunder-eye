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

    /**
     * 添加服务器配置
     *
     * 此方法用于处理服务器配置的添加请求首先检查数据库中是否已存在相同IP的服务器配置，
     * 如果存在，则返回错误信息如果不存在，则构建新的服务器配置对象，并将其插入数据库中，
     * 最后返回成功信息
     *
     * @param condition 请求条件，包含服务器配置的相关信息
     * @return ResponseEntity<?> 返回添加结果，包括状态码和消息
     */
    @Override
    public ResponseEntity<?> add(RequestCondition condition) {
        // 检查数据库中是否已存在相同IP的服务器配置
        QueryWrapper<ServerConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("ip", condition.getIp());
        Long count = serverMapper.selectCount(wrapper);
        if (count > 0) {
            // 如果存在相同IP的服务器配置，返回错误信息
            return new ResponseEntity<>()
                    .fail("服务器已存在");
        }

        // 构建新的服务器配置对象
        ServerConfig serverConfig = ServerConfig.builder()
                .ip(condition.getIp())
                .serverType(condition.getServerType())
                .name(condition.getName())
                .username(condition.getUser())
                .password(condition.getPassword())
                .enable(false)
                .dtime(DateUtil.date())
                .build();

        // 将新的服务器配置插入数据库
        int i = serverMapper.insert(serverConfig);

        // 返回添加结果，包括状态码和消息
        return new ResponseEntity<>().success(i, "添加成功");
    }

    /**
     * 删除指定资源
     *
     * 此方法用于根据条件删除特定资源首先检查传入的条件中索引是否为空或未提供，
     * 如果索引无效，则返回失败响应如果索引有效，则执行删除操作并返回成功响应
     *
     * @param condition 删除资源的条件，包含资源的索引信息
     * @return ResponseEntity<?> 返回一个响应实体，表示删除操作的结果
     */
    @Override
    public ResponseEntity<?> del(RequestCondition condition) {
        // 检查传入的索引是否为空或未提供，如果为空则返回参数错误的响应
        if (condition.getIndex() == null || "".equals(condition.getIndex())) {
            return new ResponseEntity<>().fail("参数错误");
        }

        // 索引有效，执行删除操作并返回删除成功的响应
        return new ResponseEntity<>().success(serverMapper.deleteById(condition.getIndex()), "删除成功");
    }

    /**
     * 更新服务器配置
     *
     * 根据提供的条件更新服务器配置信息此方法首先检查提供的索引是否有效，然后构建一个ServerConfig对象，
     * 根据条件更新其属性最后，调用updateById方法更新数据库中的相应记录
     *
     * @param condition 包含要更新的服务器配置信息的请求条件
     * @return ResponseEntity<?> 返回一个包含更新结果的响应实体
     */
    @Override
    public ResponseEntity<?> update(RequestCondition condition) {
        // 检查索引是否为空或无效
        if (condition.getIndex() == null || "".equals(condition.getIndex())) {
            return new ResponseEntity<>().fail("参数错误");
        }

        // 创建一个ServerConfig构建器
        ServerConfig.ServerConfigBuilder serverConfig = ServerConfig.builder();
        // 设置配置的ID
        serverConfig.id(Long.valueOf(condition.getIndex()));

        // 根据条件更新配置的IP地址
        if (StringUtils.hasText(condition.getIp() )) {
            serverConfig.ip(condition.getIp());
        }

        // 根据条件更新配置的服务器类型
        if (StringUtils.hasText(condition.getServerType())) {
            serverConfig.serverType(condition.getServerType());
        }

        // 根据条件更新配置的名称
        if (StringUtils.hasText(condition.getName())) {
            serverConfig.name(condition.getName());
        }

        // 根据条件更新配置的用户名
        if (StringUtils.hasText(condition.getUser())) {
            serverConfig.username(condition.getUser());
        }

        // 根据条件更新配置的密码
        if (StringUtils.hasText(condition.getPassword() )) {
            serverConfig.password(condition.getPassword());
        }

        // 根据条件更新配置的启用状态
        if (condition.getEnable() != null) {
            serverConfig.enable(condition.getEnable());
        }

        // 构建最终的ServerConfig对象并设置更新时间
        ServerConfig serverConfigB = serverConfig.dtime(DateUtil.date()).build();

        // 调用Mapper方法更新数据库中的配置
        int i = serverMapper.updateById(serverConfigB);

        // 返回更新结果
        return new ResponseEntity<>().success(i, "修改成功");
    }

    /**
     * 根据请求条件获取单个实体信息
     *
     * 此方法根据传入的RequestCondition对象中的条件，查询并返回一个ServerConfig对象
     * 它通过构建一个QueryWrapper来根据不同的条件进行查询，体现了代码的灵活性和可扩展性
     *
     * @param condition 请求条件，包含查询所需的条件如索引、IP和用户名
     * @return 返回包含查询结果的ResponseEntity对象
     */
    @Override
    public ResponseEntity<?> getOne(RequestCondition condition) {
        // 初始化查询包装器
        QueryWrapper<ServerConfig> wrapper = new QueryWrapper<>();

        // 如果条件中的索引值非空，则添加ID查询条件
        if (condition.getIndex() != null && !"".equals(condition.getIndex())) {
            wrapper.eq("id", condition.getIndex());
        }

        // 如果条件中的IP非空，则添加IP查询条件
        if (condition.getIp() != null && !"".equals(condition.getIp())) {
            wrapper.eq("ip", condition.getIp());
        }

        // 如果条件中的用户名非空，则添加用户名查询条件
        if (condition.getUser() != null && !"".equals(condition.getUser())) {
            wrapper.eq("username", condition.getUser());
        }

        // 执行查询，获取一个ServerConfig对象
        ServerConfig serverConfig = serverMapper.selectOne(wrapper);

        // 返回查询结果及成功信息
        return new ResponseEntity<>().success(serverConfig, "查询成功");
    }


    /**
     * 获取所有服务器信息
     *
     * 该方法用于查询并返回数据库中所有的服务器信息它不接受任何参数，
     * 并返回一个包含查询结果和成功消息的ResponseEntity对象
     *
     * @return ResponseEntity<?> 返回一个包含服务器信息列表和成功消息的响应对象
     */
    @Override
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>().success(serverMapper.selectList(null), "查询成功");
    }

    @Override
    public ResponseEntity<?> getPage(RequestCondition condition) {
        QueryWrapper<ServerConfig> wrapper = new QueryWrapper<>();
        return null;
    }

    /**
     * 获取所有启用的服务器配置
     *
     * 该方法通过查询数据库中所有enable字段为true的ServerConfig记录来实现
     * 使用了MyBatis Plus的QueryWrapper对象来构建查询条件，以确保只获取启用状态的配置
     *
     * @return 包含启用的服务器配置列表的ResponseEntity对象
     */
    @Override
    public ResponseEntity<?> getAllEnable() {
        // 创建QueryWrapper对象，并设置查询条件为enable字段等于true
        QueryWrapper<ServerConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("enable", true);

        // 执行查询并返回查询结果，附带成功消息
        return new ResponseEntity<>().success(serverMapper.selectList(wrapper), "查询成功");
    }
}
