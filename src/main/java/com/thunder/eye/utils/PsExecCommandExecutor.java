package com.thunder.eye.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * PsExecCommandExecutor 类的简要描述
 *
 * @author liyang
 * @since 2025/1/23
 */
@Slf4j
public class PsExecCommandExecutor {
    private final String remoteComputer;
    private final String username;
    private final String password;
    private final String psExecPath;


    public PsExecCommandExecutor(String remoteComputer, String username, String password) throws IOException {
        this.username = username;
        this.password = password;
        this.remoteComputer = remoteComputer;
        this.psExecPath = "C:\\opt\\settings\\PsExec.exe";
    }

    /**
     * 获取 psexec.exe 的绝对路径
     */
    private String getPsexecPath() throws IOException {
        // 获取资源文件输入流
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("psexec.exe");
        if (inputStream == null) {
            throw new IOException("无法找到 psexec.exe 文件");
        }

        // 创建临时文件
        Path tempFile = Files.createTempFile("psexec", ".exe");
        try (FileOutputStream outputStream = new FileOutputStream(tempFile.toFile())) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } finally {
            inputStream.close();
        }

        // 设置临时文件在程序结束时自动删除
        tempFile.toFile().deleteOnExit();

        return tempFile.toString();
    }

    //    public static void main(String[] args) throws IOException {
//        PsExecCommandExecutor executor = new PsExecCommandExecutor("192.168.200.26", "administrator", "\"PTJK123qwe,.26\"");
//        String result = executor.exec("\"ipconfig\"");
//        System.out.println(result);
//    }
    public static void main(String[] args) {
        try {
            // 定义远程计算机信息和要执行的命令
            String remoteComputer = "192.168.200.26"; // 替换为实际的远程计算机名或IP地址
            String command = "ipconfig /all";       // 替换为实际要执行的命令

            // 构建 PowerShell 命令
            String powershellCommand = String.format(
                    "powershell.exe -Command \"Invoke-Command -ComputerName '%s' -ScriptBlock { %s }\"",
                    remoteComputer, command);

            // 使用 ProcessBuilder 执行命令
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", powershellCommand);
            pb.redirectErrorStream(true); // 合并标准输出和错误流
            Process process = pb.start();

            // 读取命令输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 等待进程结束并获取退出码
            int exitCode = process.waitFor();
            System.out.println("Exit Code: " + exitCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用 PsExec 在远程计算机上执行命令。
     *
     * @param command 要执行的命令字符串
     * @return 命令的输出结果
     */
    public String exec(String command) {
        StringBuilder output = new StringBuilder();
        try {
            // 构建 PsExec 命令
            List<String> commandList = Arrays.asList(
                    "cmd.exe", "/c",
                    psExecPath, "-i", "\\\\" + remoteComputer, "-u", username, "-p", password, "-accepteula", command
            );
            System.out.println(String.join(" ", commandList));

            // 使用 ProcessBuilder 执行命令
            ProcessBuilder pb = new ProcessBuilder(commandList);
            pb.redirectErrorStream(true);
            Process process = pb.start();
// 同时捕获标准输出和标准错误流
            String line;


            // 读取命令输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            while ((line = reader.readLine()) != null) {
                System.out.println(line);  // 添加调试输出
                output.append(line).append("\n");
            }

            // 等待进程结束并获取退出码
            int exitCode = process.waitFor();
            output.append("Exit Code: ").append(exitCode).append("\n");

        } catch (IOException | InterruptedException e) {
            log.error("An error occurred while executing the command.", e);
            output.append("An error occurred while executing the command.\n").append(e.getMessage());
        }

        return output.toString();
    }


    public String getRemoteComputer() {
        return remoteComputer;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
