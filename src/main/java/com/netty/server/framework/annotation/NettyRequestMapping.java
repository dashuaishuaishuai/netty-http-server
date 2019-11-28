/**
 * Copyright (C), 2015-2019
 * FileName: NettyRequestMapping
 * Project Name:netty-http-server
 * Date:     2019/11/28 9:11
 * Description:
 */
package com.netty.server.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 〈一句话功能简述〉
 * 〈〉
 *
 * @author mashuai
 * @version 2019/11/28
 * @since JDK1.8
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NettyRequestMapping {
    String value() default "";
}