/**
 * Copyright (C), 2015-2019
 * FileName: LoginController
 * Project Name: netty-http-server
 * Date:     2019/11/28 9:10
 * Description:
 */
package com.netty.server.module.login;

import com.netty.server.framework.annotation.NettyController;
import com.netty.server.framework.annotation.NettyRequestMapping;

/**
 * 〈一句话功能简述〉
 * 〈〉
 *
 * @author mashuai
 * @version 2019/11/28
 * @since JDK1.8
 */
@NettyController("/login")
public class LoginController {

    @NettyRequestMapping("/")
    public String login(String userName, String passWord) {

        return null;
    }
}