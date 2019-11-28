/**
 * Copyright (C), 2015-2019
 * FileName: Constants
 * Project Name:netty-http-server
 * Date:     2019/11/27 16:54
 * Description:
 */
package com.netty.server.framework.constants;

import java.io.File;

/**
 * 常量类
 *
 * @author mashuai
 * @version 2019/11/27
 * @since JDK1.8
 */
public interface SysConstants {

    String BASE_PATH = System.getProperty("app.home", System.getProperty("user.dir")) + File.separator + "src" + File.separator + "webapp";
    String[] STATIC_SOURCE = {"html", "jpg", "js", "css", "txt","pdf"};
    String BASE_PACKAGE = "com.netty.server.module";

}