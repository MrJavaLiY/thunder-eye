package com.thunder.eye.service;

import com.thunder.eye.entity.sql.ServerConfig;
import com.thunder.eye.utils.ShellUtil;

public interface ShellService {

    ShellUtil getShellUtil(ServerConfig serverConfig) throws Exception;
    void closeShell()throws Exception;
}
