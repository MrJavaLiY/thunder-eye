package com.thunder.eye.service.impl;

import com.thunder.eye.entity.entity.JarDetailEntity;
import com.thunder.eye.entity.sql.ServerConfig;
import com.thunder.eye.mode.StrategyOperate;
import com.thunder.eye.service.HeartbeatService;
import com.thunder.eye.service.ServerConfigService;
import com.thunder.eye.utils.CacheUtil;
import com.thunder.eye.utils.CaffeineUtil;
import com.thunder.eye.utils.ResponseEntity;
import com.xiaoleilu.hutool.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 心跳服务实现类
 */
@Service
@Slf4j
public class HeartbeatServiceImpl implements HeartbeatService {
    @Resource
    StrategyOperate strategyOperate;
    @Resource
    ServerConfigService serverConfigService;
    CacheUtil cacheUtil;

    @Override
    public CacheUtil getCacheUtil() {
        return cacheUtil;
    }

    @PostConstruct
    public void init() {
        cacheUtil = new CaffeineUtil();
    }

    @Override
    public void dispatch() {
        Date now = DateUtil.date();
        try {
            List<ServerConfig> serverConfigs = (List<ServerConfig>) serverConfigService.getAllEnable().getData();
            for (ServerConfig serverConfig : serverConfigs) {
                //每一台服务器上面的服务信息
                List<JarDetailEntity> nowData = this.getJarDetailEntities(serverConfig);
                //这一步进行的是数据清洗，将本次拿回来的数据进行缓存
                this.extracted(serverConfig, nowData, now);
            }
        } catch (Exception e) {
            log.error("发生错误", e);
        }
    }


    /**
     * 清洗更新
     *
     * @param serverConfig
     * @param nowData
     * @param now
     */
    private void extracted(ServerConfig serverConfig, List<JarDetailEntity> nowData, Date now) {
        List<JarDetailEntity> tempData = nowData;
        if (cacheUtil.exists(serverConfig.getIp())) {
            List<JarDetailEntity> oldData = cacheUtil.get(serverConfig.getIp());
            //循环新的，如果新的数据里有老的，证明服务一直在线，更新在线时间
            for (JarDetailEntity nowDatum : nowData) {
                nowDatum.setLastTime(now);
                boolean isExist = false;
                for (JarDetailEntity oldDatum : oldData) {
                    if (nowDatum.getServerIndex().equals(oldDatum.getServerIndex())) {
                        nowDatum.setSurStatus(JarDetailEntity.ONLINE);
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) nowDatum.setSurStatus(JarDetailEntity.NEW_ONLINE);
            }
            //循环老的，如果新的里面没有老的，说明这条掉线了
            for (JarDetailEntity oldDatum : oldData) {
                boolean isExist = false;
                for (JarDetailEntity nowDatum : nowData) {
                    if (nowDatum.getServerIndex().equals(oldDatum.getServerIndex())) {
                        nowDatum.setSurStatus(JarDetailEntity.ONLINE);
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    oldDatum.setSurStatus(JarDetailEntity.OFFLINE);
                    tempData.add(oldDatum);
                }
            }
        }
        cacheUtil.put(serverConfig.getIp(), tempData, 60 * 60 * 24);
    }

    /**
     * 通过策略模式去获取远程的JSCH链接，得到数据
     *
     * @param serverConfig
     * @return
     * @throws Exception
     */
    private List<JarDetailEntity> getJarDetailEntities(ServerConfig serverConfig) throws Exception {
        Object o = strategyOperate.
                executeMethodSpring(serverConfig.getServerType(), serverConfig);
        ResponseEntity<List<JarDetailEntity>> responseEntity = (ResponseEntity<List<JarDetailEntity>>) o;
        return responseEntity.getData();
    }
}

