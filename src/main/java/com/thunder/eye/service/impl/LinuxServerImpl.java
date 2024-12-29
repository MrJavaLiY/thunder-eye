package com.thunder.eye.service.impl;


import com.thunder.eye.entity.entity.JarDetailEntity;
import com.thunder.eye.entity.sql.ServerConfig;
import com.thunder.eye.service.LinuxService;
import com.thunder.eye.utils.ResponseEntity;
import com.thunder.eye.utils.ShellUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class LinuxServerImpl implements LinuxService {

    @Override
    public ResponseEntity<List<JarDetailEntity>> dispatch(ServerConfig serverConfig) throws Exception {
        log.debug("Linux服务器[{}]开始建立链接", serverConfig.getIp());
        ShellUtil shell = new ShellUtil(serverConfig.getIp(), serverConfig.getUsername(), serverConfig.getPassword());
        log.debug("链接建立成功");
        String jpsValue = shell.exec("jps -l");
        log.debug("===========jps=======");
        log.debug(jpsValue);
        log.debug("=====================");
        String[] jpes = jpsValue.split("\n");
        List<JarDetailEntity> linuxServiceCmdEntities = new ArrayList<>();
        for (String jpe : jpes) {
            if (jpe.contains("jps")) {
                continue;
            }
            JarDetailEntity entity = new JarDetailEntity();
            String[] jps1 = jpe.split(" ");
            entity.setPid(Integer.parseInt(jps1[0]));
            this.getjarMessage(shell, entity);
            if (entity.getPort() == 0) {
                continue;
            }
            entity.setServerIndex(serverConfig.getIp() + ":" + entity.getPort());
            entity.setLastTime(new Date());
            entity.setSurStatus(1);
            linuxServiceCmdEntities.add(entity);
        }

        return new ResponseEntity<List<JarDetailEntity>>().success(linuxServiceCmdEntities, "s");
    }

    private void getjarMessage(ShellUtil shell, JarDetailEntity entity) throws Exception {
        String jvmmes = shell.exec("jcmd " + entity.getPid() + " VM.command_line");
        if (jvmmes.contains("org.jetbrains")) {
            return;
        }
        log.debug("===========jcmd=======");
        log.debug(jvmmes);
        log.debug("=====================");
        String[] jvmesValues = jvmmes.split("java_command:");

        if (jvmesValues.length > 1) {
            String[] javaCommands = jvmesValues[1].split("java_class_path");
            String jarName = javaCommands[0].replaceAll(" ", "").replaceAll("\n", "");
            String jarPath = "";
            entity.setJarName(jarName);
            entity.setJarPath(jarPath);
        }
        this.getPath(shell, entity);
        this.getPost(shell, entity);
    }

    private void getPath(ShellUtil shell, JarDetailEntity entity) throws Exception {
        String pwdx = "pwdx " + entity.getPid();
        String pwdxValue = shell.exec(pwdx);
        log.debug("===========pwdx=======");
        log.debug(pwdxValue);
        log.debug("=====================");
        entity.setJarPath(pwdxValue.substring(pwdxValue.indexOf("/")).replaceAll("\n", ""));
    }

    private void getPost(ShellUtil shell, JarDetailEntity entity) throws Exception {
        if (entity.getJarName().contains("org.jetbrains")) {
            return;
        }
        String sudo = " sudo lsof -i -P -n |grep " + entity.getPid();
        String sudoValue = shell.exec(sudo);
        log.debug("===========sudo=======");
        log.debug(sudoValue);
        log.debug("=====================");
        String[] sudoValues = sudoValue.split("\\*:");
        String port = sudoValues[1].substring(0, sudoValues[1].indexOf("(")).replaceAll(" ", "");
        entity.setPort(Integer.parseInt(port));
    }


}
