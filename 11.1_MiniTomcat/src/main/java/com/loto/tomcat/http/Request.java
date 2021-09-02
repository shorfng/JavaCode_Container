package com.loto.tomcat.http;

import lombok.Data;

import java.io.IOException;
import java.io.InputStream;

/**
 * 把请求信息封装为Request对象（根据InputSteam输入流封装）
 */
@Data
public class Request {
    private String method;  // 请求方式（GET/POST）
    private String url;
    private InputStream inputStream;  // 输入流（其他属性从输入流中解析出来）

    public Request() {
    }

    // 构造器，输入流传入
    public Request(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;

        // 从输入流中获取请求信息
        int count = 0;
        while (count == 0) {
            count = inputStream.available();
        }

        byte[] bytes = new byte[count];
        inputStream.read(bytes);

        // 获取第一行请求头信息
        String inputStr = new String(bytes);
        String firstLineStr = inputStr.split("\\n")[0];  // GET / HTTP/1.1
        String[] strings = firstLineStr.split(" ");

        this.method = strings[0];
        this.url = strings[1];

        System.out.println("=====>>method:" + method);
        System.out.println("=====>>url:" + url);
    }
}
