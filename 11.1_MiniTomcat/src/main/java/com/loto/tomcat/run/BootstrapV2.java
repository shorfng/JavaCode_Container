package com.loto.tomcat.run;

import com.loto.tomcat.http.Request;
import com.loto.tomcat.http.Response;
import com.loto.tomcat.util.HttpProtocolUtil;
import lombok.Data;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Author：蓝田_Loto
 * <p>Date：2021-09-02 19:36</p>
 * <p>PageName：BootstrapV2.java</p>
 * <p>Function：启动类</p>
 */

@Data
public class BootstrapV2 {
    /**
     * 定义 socket 监听的端口号
     */
    private int port = 8080;

    /**
     * Minicat 的程序启动入口
     */
    public static void main(String[] args) {
        BootstrapV2 bootstrap = new BootstrapV2();

        try {
            // 启动 Minicat
            bootstrap.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Minicat 2.0版本：封装Request和Response对象，返回html静态资源文件
    // http://localhost:8080/index.html
    /**
     * Minicat 启动需要初始化展开的一些操作
     */
    public void start() throws Exception {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("=====>>> Minicat start on port：" + port);

        while (true) {
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();

            // 封装Request对象和Response对象
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());

            response.outputHtml(request.getUrl());
            socket.close();
        }
    }
}
