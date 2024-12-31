package com.thunder.eye.service.impl;

import com.thunder.eye.entity.sql.ServerConfig;
import com.thunder.eye.service.ShellService;
import com.thunder.eye.utils.CacheUtil;
import com.thunder.eye.utils.CaffeineUtil;
import com.thunder.eye.utils.ShellUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

@Slf4j
@Service
public class ShellServiceImpl implements ShellService {

    CacheUtil cacheUtil;

    /**
     * 初始化方法，用于在Bean实例化后设置缓存工具
     * 该方法通过@PostConstruct注解标记，确保在Bean的构造函数调用之后，其他方法调用之前执行
     *
     * @see CaffeineUtil
     */
    @PostConstruct
    public void init() {
        cacheUtil = new CaffeineUtil();
    }

    @Override
    public ShellUtil getShellUtil(ServerConfig serverConfig) throws Exception {
        return getShell(serverConfig);
    }

    /**
     * 获取与Linux服务器的Shell连接
     * 如果缓存中已存在该服务器的连接，则直接获取；否则，创建新的连接
     * 并将其保存在缓存中
     *
     * @param serverConfig 服务器配置信息，包括IP地址、用户名和密码
     * @return ShellUtil对象，用于与Linux服务器进行交互
     * @throws Exception 如果创建新的Shell连接时发生错误，则抛出异常
     */
    private ShellUtil getShell(ServerConfig serverConfig) throws Exception {
        ShellUtil shell;
        // 检查缓存中是否存在与该服务器的连接
        if (cacheUtil.exists(serverConfig.getIp())) {
            // 如果存在，则从缓存中获取Shell连接
            shell = cacheUtil.get(serverConfig.getIp());
            log.debug("{}服务器[{}]链接获取成功", serverConfig.getServerType(), serverConfig.getIp());
        } else {
            // 如果不存在，则记录开始建立连接的日志
            log.debug("{}服务器[{}]开始建立链接", serverConfig.getServerType(), serverConfig.getIp());
            // 创建新的Shell连接
            shell = new ShellUtil(serverConfig.getIp(), serverConfig.getUsername(), serverConfig.getPassword());
            log.debug("[{}]链接建立成功", serverConfig.getIp());
        }
        // 将Shell连接保存到缓存中，这里给到24小时缓存过期，但是实际上会有1个任务作业在10分钟左右给他手动释放掉，避免一个链接用的太久造成一些不可预知的问题
        cacheUtil.put(serverConfig.getIp(), shell, 60 * 60 * 24);
        // 返回缓存中的Shell连接
        return cacheUtil.get(serverConfig.getIp());
    }
@Override
    public synchronized void closeShell() throws Exception {
        Map<String, Object> shellUtilMap = cacheUtil.getAll();
        shellUtilMap.forEach((k, v) -> {
            try {
                ShellUtil shellUtil = (ShellUtil) v;
                shellUtil.close();
                cacheUtil.delete(k);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
