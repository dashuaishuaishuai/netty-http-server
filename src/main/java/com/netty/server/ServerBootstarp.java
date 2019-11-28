/**
 * Copyright (C), 2015-2019
 * FileName: ServerBootstarp
 * Project Name: netty-http-server
 * Date:     2019/11/27 16:51
 * Description:
 */
package com.netty.server;

import com.netty.server.framework.httpserver.HttpServerContainer;
import com.netty.server.framework.instance.ClassInstanceContainer;

/**
 * 〈一句话功能简述〉
 * 〈〉
 *
 * @author mashuai
 * @version 2019/11/27
 * @since JDK1.8
 */
public class ServerBootstarp {

    public static void main(String[] args) throws InterruptedException {
        HttpServerContainer httpServerContainer = new HttpServerContainer();
        ClassInstanceContainer classInstanceContainer = new ClassInstanceContainer();
        classInstanceContainer.start();
        new Thread(()->{
            httpServerContainer.start(args);
        }).start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            httpServerContainer.stop(args);
        }));

    }

}