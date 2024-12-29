package com.thunder.eye.entity.sql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author ly
 */
@Data
@TableName("server_config")
@Builder
public class ServerConfig {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "name")
    private String name;
    @TableField(value = "ip")
    private String ip;
    @TableField(value = "username")
    private String username;
    @TableField(value = "password")
    private String password;
    @TableField(value = "server_type")
    private String serverType;
    @TableField(value = "enable")
    private Boolean enable;
    @TableField(value = "dtime")
    private Date dtime;
}
