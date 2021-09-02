package com.loto.tomcat.servlet;

import com.loto.tomcat.http.Request;
import com.loto.tomcat.http.Response;
import com.loto.tomcat.util.HttpProtocolUtil;

import java.io.IOException;

public class MyServlet extends HttpServlet {
    @Override
    public void init() throws Exception {
    }

    @Override
    public void destory() throws Exception {
    }

    @Override
    public void doGet(Request request, Response response) {
        String content = "<h1>MyServlet get</h1>";

        try {
            response.output((HttpProtocolUtil.getHttpHeader200(content.getBytes().length) + content));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(Request request, Response response) {
        String content = "<h1>MyServlet post</h1>";

        try {
            response.output((HttpProtocolUtil.getHttpHeader200(content.getBytes().length) + content));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
