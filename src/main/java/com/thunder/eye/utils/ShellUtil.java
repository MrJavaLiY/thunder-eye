package com.thunder.eye.utils;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

@Slf4j
public class ShellUtil {
    private volatile Session session;
    private final String ip;
    private final String user;
    private final String password;

    public ShellUtil(String host, String user, String password) throws JSchException {
        this.ip = host;
        this.user = user;
        this.password = password;
        extracted(host, user, password);

    }

    private synchronized void extracted(String host, String user, String password) throws JSchException {
        this.session = new JSch().getSession(user, host, 22);
        session.setPassword(password);
        //首选通过密码进行身份认证，否则建立连接会花费很久
        session.setConfig("PreferredAuthentications", "password");
        //不进行严格的主机密钥检查
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
    }

    public String exec(String command) throws Exception {

        if (session == null) {
            synchronized (this) {
                if (session == null) {
                    if (isValidInput(ip, user, password)) {
                        try {
                            extracted(ip, user, password);
                        } catch (Exception e) {
                            // 记录日志并处理异常
                            log.error("Failed to extract session", e);
                            throw new RuntimeException("Failed to extract session", e);
                        }
                    } else {
                        throw new IllegalArgumentException("Invalid input parameters");
                    }
                }
            }
        }

        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);
        channel.setInputStream(null);
        ((ChannelExec) channel).setErrStream(System.err);
        InputStream in = channel.getInputStream();
        // Exec thread 启动
        channel.connect(10000);
        //有些 command 可能执行很长时间, 读取执行结果时需要等待
        StringBuilder res = new StringBuilder();
        byte[] tmp = new byte[1024];
        while (in.available() > 0 || !channel.isClosed()) {
            if (!channel.isClosed()) {
                Thread.sleep(200);
            }
            int red = in.read(tmp, 0, 1024);
            if (red == -1) {
                continue;
            }
            String s = new String(tmp, 0, red);
            res.append(s);
        }

        return res.toString();
    }

    public synchronized void close() throws JSchException {
        Channel channel = session.openChannel("exec");
        channel.disconnect();
        session.disconnect();
        session = null;
    }

    private boolean isValidInput(String ip, String user, String password) {
        return ip != null && !ip.trim().isEmpty() &&
                user != null && !user.trim().isEmpty() &&
                password != null && !password.trim().isEmpty();
    }
}
