package com.thunder.eye.service.impl;


import com.thunder.eye.entity.entity.JarDetailEntity;
import com.thunder.eye.entity.sql.ServerConfig;
import com.thunder.eye.service.WinService;
import com.thunder.eye.utils.ResponseEntity;
import com.thunder.eye.utils.ShellUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@Slf4j
public class WinServerImpl implements WinService {


    @Override
    public ResponseEntity<List<JarDetailEntity>> dispatch(ServerConfig serverConfig) throws Exception {
        log.debug("windows服务器[{}]开始建立链接", serverConfig.getIp());
        ShellUtil shell = new ShellUtil(serverConfig.getIp(), serverConfig.getUsername(), serverConfig.getPassword());
        String jpsValue = shell.exec("jps -l");
        log.debug("===========jps=======");
        log.debug(jpsValue);
        log.debug("=====================");
        String[] jpes = jpsValue.split("\n");
        List<JarDetailEntity> winCmdEntities = new ArrayList<>();
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
            winCmdEntities.add(entity);
        }

        return new ResponseEntity<List<JarDetailEntity>>().success(winCmdEntities, "s");
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
            String jarNamePath = javaCommands[0].replaceAll(" ", "").replaceAll("\n", "");
            log.debug("jarNamePath" + jarNamePath);
            String jarPath = "";
            String jarName = "";
            if (jarNamePath.contains("\\")) {
                jarPath = jarNamePath.substring(0, jarNamePath.lastIndexOf("\\")).replaceAll("\n", "");
                jarName = jarNamePath.substring(jarNamePath.lastIndexOf("\\") + 1).replaceAll("\n", "");
            } else {
                entity.setJarName(jarNamePath);
            }
            entity.setJarPath(jarPath);
        }
        this.getPost(shell, entity);
    }

    private void getPost(ShellUtil shell, JarDetailEntity entity) throws Exception {
        if (entity.getJarName()!=null&&entity.getJarName().contains("org.jetbrains")) {
            return;
        }
        String netstat = "netstat -aon |findstr " + entity.getPid();
        String netstats = shell.exec(netstat.toString());
        log.debug("===========netstat=======");
        log.debug(netstats);
        log.debug("=====================");
        String[] netstatValues = netstats.split("TCP");
        String netstatValue = netstatValues[1];
        String ss = "0.0.0.0:";
        if (netstatValue.contains(ss)) {
            String[] as = netstatValue.replaceAll(" ", "").split(ss);
            if (isInteger(as[1])) {
                entity.setPort(Integer.parseInt(as[1].replaceAll(" ", "")));
            }


        }
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }


}
