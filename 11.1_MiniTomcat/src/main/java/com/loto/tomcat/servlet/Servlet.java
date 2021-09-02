package com.loto.tomcat.servlet;

import com.loto.tomcat.http.Request;
import com.loto.tomcat.http.Response;

public interface Servlet {
    void init() throws Exception;
    void destory() throws Exception;
    void service(Request request, Response response) throws Exception;
}
