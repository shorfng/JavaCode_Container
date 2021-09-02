package com.loto.tomcat.run;

import com.loto.tomcat.util.HttpProtocolUtil;
import lombok.Data;

import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Author：蓝田_Loto
 * <p>Date：2021-09-02 19:36</p>
 * <p>PageName：BootstrapV1.java</p>
 * <p>Function：启动类</p>
 */

@Data
public class BootstrapV1 {
    /**
     * 定义 socket 监听的端口号
     */
    private int port = 8081;

    /**
     * Minicat 的程序启动入口
     */
    public static void main(String[] args) {
        BootstrapV1 bootstrap = new BootstrapV1();

        try {
            // 启动 Minicat
            bootstrap.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Minicat 1.0版本：浏览器请求 http://localhost:8081，返回一个固定的字符串到页面 "Hello Minicat!"
    /**
     * Minicat 启动需要初始化展开的一些操作
     */
    public void start() throws Exception {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("=====>>> Minicat start on port：" + port);

        while (true) {
            Socket socket = serverSocket.accept();

            // 获取输出流
            OutputStream outputStream = socket.getOutputStream();

            String data = "Hello Minicat!";
            String responseText = HttpProtocolUtil.getHttpHeader200(data.getBytes().length) + data;
            outputStream.write(responseText.getBytes());
            socket.close();
        }
    }
}
