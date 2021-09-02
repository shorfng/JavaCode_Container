package com.loto.tomcat.run;

import com.loto.tomcat.http.RequestProcessor;
import com.loto.tomcat.servlet.HttpServlet;
import lombok.Data;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author：蓝田_Loto
 * <p>Date：2021-09-02 19:36</p>
 * <p>PageName：BootstrapV5.java</p>
 * <p>Function：启动类</p>
 */

@Data
public class BootstrapV5 {
    /**
     * 定义 socket 监听的端口号
     */
    private int port = 8085;

    private Map<String, HttpServlet> servletMap = new HashMap<>();

    /**
     * Minicat 的程序启动入口
     */
    public static void main(String[] args) {
        BootstrapV5 bootstrap = new BootstrapV5();

        try {
            // 启动 Minicat
            bootstrap.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Minicat 5.0版本：多线程改造（使用线程池）
    // http://localhost:8085/index.html

    /**
     * Minicat 启动需要初始化展开的一些操作
     */
    public void start() throws Exception {
        // 加载解析相关的配置，web.xml
        loadServlet();

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("=====>>> Minicat start on port：" + port);

        // 定义一个线程池
        int corePoolSize = 10;
        int maximumPoolSize = 50;
        long keepAliveTime = 100L;
        TimeUnit unit = TimeUnit.SECONDS;   // 单位：秒
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(50);   // 请求队列（长度50）
        ThreadFactory threadFactory = Executors.defaultThreadFactory();             // 线程工厂
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();    // 拒绝策略

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                threadFactory,
                handler
        );

        // 多线程改造（使用线程池）
        while (true) {
            Socket socket = serverSocket.accept();
            RequestProcessor requestProcessor = new RequestProcessor(socket, servletMap);
            threadPoolExecutor.execute(requestProcessor);
        }
    }

    /**
     * 加载解析web.xml，初始化Servlet
     */
    private void loadServlet() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();

            List<Element> selectNodes = rootElement.selectNodes("//servlet");
            for (int i = 0; i < selectNodes.size(); i++) {
                Element element = selectNodes.get(i);
                // <servlet-name>my</servlet-name>
                Element servletnameElement = (Element) element.selectSingleNode("servlet-name");
                String servletName = servletnameElement.getStringValue();

                // <servlet-class>com.loto.tomcat.servlet.MyServlet</servlet-class>
                Element servletclassElement = (Element) element.selectSingleNode("servlet-class");
                String servletClass = servletclassElement.getStringValue();

                // 根据 servlet-name 的值找到 url-pattern
                // <servlet-mapping>
                Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");

                // <url-pattern>/my</url-pattern>
                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();

                servletMap.put(urlPattern, (HttpServlet) Class.forName(servletClass).newInstance());
            }
        } catch (DocumentException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
