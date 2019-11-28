/**
 * Copyright (C), 2015-2019
 * FileName: Container
 * Project Name: netty-http-server
 * Date:     2019/11/27 16:58
 * Description:
 */
package com.netty.server;

/**
 * 〈一句话功能简述〉
 * 〈〉
 *
 * @author mashuai
 * @version 2019/11/27
 * @since JDK1.8
 */
public interface Container {

    void start(String... arg);

    void stop(String... arg);

}