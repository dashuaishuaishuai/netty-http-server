/**
 * Copyright (C), 2015-2019
 * FileName: ClassInstanceContainer
 * Project Name: netty-http-server
 * Date:     2019/11/28 9:41
 * Description:
 */
package com.netty.server.framework.instance;

import com.netty.server.Container;
import com.netty.server.framework.constants.SysConstants;
import lombok.extern.slf4j.Slf4j;

/**
 * 〈一句话功能简述〉
 * 〈〉
 *
 * @author mashuai
 * @version 2019/11/28
 * @since JDK1.8
 */
@Slf4j
public class ClassInstanceContainer implements Container {


    @Override
    public void start(String... arg) {
        new ClassInstanceHandler().getModuleClass(SysConstants.BASE_PACKAGE).getNeedInstanceClass().initHandlerMapping();
    }


    @Override
    public void stop(String... arg) {

    }
}