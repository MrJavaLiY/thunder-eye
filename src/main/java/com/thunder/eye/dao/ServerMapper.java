package com.thunder.eye.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thunder.eye.entity.sql.ServerConfig;
import org.springframework.stereotype.Repository;

/**
 * @author ly
 * @date 2024年12月28日16:16:38
 */
@Repository
public interface ServerMapper extends BaseMapper<ServerConfig> {
}
