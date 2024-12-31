package com.thunder.eye.service.impl;


import com.thunder.eye.entity.entity.JarDetailEntity;
import com.thunder.eye.entity.sql.ServerConfig;
import com.thunder.eye.service.LinuxService;
import com.thunder.eye.service.ShellService;
import com.thunder.eye.utils.ResponseEntity;
import com.thunder.eye.utils.ShellUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class LinuxServerImpl implements LinuxService {
    @Resource
    ShellService shellService;

    /**
     * 根据服务器配置信息获取正在运行的Java进程详细信息
     *
     * @param serverConfig 服务器配置信息，包含连接服务器所需的信息
     * @return 返回一个包含JarDetailEntity对象的列表，每个对象包含一个Java进程的详细信息
     * @throws Exception 如果执行外部命令或处理过程中发生错误，则抛出异常
     */
    @Override
    public ResponseEntity<List<JarDetailEntity>> dispatch(ServerConfig serverConfig) throws Exception {
        // 获取与服务器的Shell连接
        ShellUtil shell = shellService.getShellUtil(serverConfig);
        // 执行jps命令获取当前运行的Java进程信息
        String jpsValue = shell.exec("jps -l");
        // 日志输出jps命令的结果，用于调试
        log.debug("===========jps=======");
        log.debug(jpsValue);
        log.debug("=====================");

        // 分割jps命令的结果，以便逐行处理
        String[] jpes = jpsValue.split("\n");
        // 创建一个列表存储JarDetailEntity对象
        List<JarDetailEntity> linuxServiceCmdEntities = new ArrayList<>();

        // 遍历jps命令的结果，处理每个Java进程信息
        for (String jpe : jpes) {
            // 忽略包含"jps"的行，因为这是jps命令自身的信息
            if (jpe.contains("jps")) {
                continue;
            }

            // 创建一个JarDetailEntity对象存储当前进程的详细信息
            JarDetailEntity entity = new JarDetailEntity();
            // 分割进程信息，获取进程ID和jar包名称
            String[] jps1 = jpe.split(" ");
            // 设置进程ID
            entity.setPid(Integer.parseInt(jps1[0]));
            // 获取jar包的详细信息
            this.getjarMessage(shell, entity);

            // 如果进程没有监听任何端口，则忽略该进程
            if (entity.getPort() == 0) {
                continue;
            }

            // 构造服务索引，格式为“IP:端口”
            entity.setServerIndex(serverConfig.getIp() + ":" + entity.getPort());
            // 设置最后更新时间
            entity.setLastTime(new Date());
            // 设置进程的运行状态
            entity.setSurStatus(1);

            // 将当前进程的详细信息添加到列表中
            linuxServiceCmdEntities.add(entity);
        }

        // 返回包含所有Java进程详细信息的列表
        return new ResponseEntity<List<JarDetailEntity>>().success(linuxServiceCmdEntities, "s");
    }

    /**
     * 获取jar文件的信息
     * 本方法通过执行shell命令获取指定进程的JVM命令行参数，进而解析出jar文件的名称和路径
     * 如果进程的JVM命令行参数中包含"org.jetbrains"，则直接返回，不进行解析
     *
     * @param shell  Shell工具对象，用于执行shell命令
     * @param entity Jar详细信息实体对象，用于存储解析出的jar文件名称和路径
     * @throws Exception 如果执行shell命令或解析过程中出现异常，则抛出此异常
     */
    private void getjarMessage(ShellUtil shell, JarDetailEntity entity) throws Exception {
        // 执行jcmd命令获取JVM启动命令行参数
        String jvmmes = shell.exec("jcmd " + entity.getPid() + " VM.command_line");

        // 如果JVM启动命令行参数中包含"org.jetbrains"，则直接返回，不进行解析
        if (jvmmes.contains("org.jetbrains")) {
            return;
        }

        // 打印日志，用于调试
        log.debug("===========jcmd=======");
        log.debug(jvmmes);
        log.debug("=====================");

        // 分割字符串，提取java_command字段后的部分
        String[] jvmesValues = jvmmes.split("java_command:");

        // 如果成功提取到java_command字段后的部分，则进一步解析
        if (jvmesValues.length > 1) {
            // 分割字符串，提取java_class_path字段前的部分
            String[] javaCommands = jvmesValues[1].split("java_class_path");
            // 通过正则表达式去除空格和换行符，得到jar文件名称
            String jarName = javaCommands[0].replaceAll(" ", "").replaceAll("\n", "");
            // 初始化jar文件路径为空字符串
            String jarPath = "";
            // 将解析出的jar文件名称和路径设置到实体对象中
            entity.setJarName(jarName);
            entity.setJarPath(jarPath);
        }

        // 调用其他方法获取更多jar文件信息
        this.getPath(shell, entity);
        this.getPost(shell, entity);
    }

    /**
     * 获取指定进程的Jar文件路径
     * 该方法通过执行shell命令来获取进程的当前工作目录，并将其设置为Jar文件的路径
     *
     * @param shell  Shell工具对象，用于执行shell命令
     * @param entity 包含Jar文件详情的实体对象，方法将获取的路径信息存储于此
     * @throws Exception 如果执行shell命令失败或解析输出异常，将抛出此异常
     */
    private void getPath(ShellUtil shell, JarDetailEntity entity) throws Exception {
        // 构造获取进程工作目录的命令
        String pwdx = "pwdx " + entity.getPid();
        // 执行命令并获取结果
        String pwdxValue = shell.exec(pwdx);

        // 打印调试信息分隔符
        log.debug("===========pwdx=======");
        // 打印命令执行结果的调试信息
        log.debug(pwdxValue);
        // 打印调试信息结束分隔符
        log.debug("=====================");

        // 从命令执行结果中提取路径信息，并设置到实体对象中
        entity.setJarPath(pwdxValue.substring(pwdxValue.indexOf("/")).replaceAll("\n", ""));
    }

    /**
     * 获取指定实体的端口信息
     * 本函数通过执行shell命令来获取指定jar详细信息实体的端口占用情况
     * 主要步骤包括：
     * 1. 检查jar名称是否包含"org.jetbrains"，如果是则直接返回，跳过后续处理
     * 2. 构建并执行shell命令，获取端口信息
     * 3. 解析命令输出，提取端口号，并设置到实体中
     *
     * @param shell  用于执行shell命令的工具对象
     * @param entity jar详细信息实体，包含jar的相关信息，如jar名称和进程ID
     * @throws Exception 如果执行shell命令或解析输出时发生错误，则抛出异常
     */
    private void getPost(ShellUtil shell, JarDetailEntity entity) throws Exception {
        // 检查jar名称是否包含"org.jetbrains"，如果是则直接返回
        if (entity.getJarName().contains("org.jetbrains")) {
            return;
        }
        // 构建用于获取端口信息的shell命令
        String sudo = " sudo lsof -i -P -n |grep " + entity.getPid();
        // 执行命令并获取输出
        String sudoValue = shell.exec(sudo);
        // 打印调试信息，用于日志记录和问题排查
        log.debug("===========sudo=======");
        log.debug(sudoValue);
        log.debug("=====================");
        // 解析命令输出，提取端口号
        String[] sudoValues = sudoValue.split("\\*:");
        String port = sudoValues[1].substring(0, sudoValues[1].indexOf("(")).replaceAll(" ", "");
        // 将端口号设置到实体中
        entity.setPort(Integer.parseInt(port));
    }


}
