/**
 * Copyright (C), 2015-2019
 * FileName: ClassInstanceHandler
 * Project Name: netty-http-server
 * Date:     2019/11/28 10:10
 * Description:
 */
package com.netty.server.framework.instance;

import com.netty.server.framework.annotation.NettyController;
import com.netty.server.framework.annotation.NettyRequestMapping;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mashuai
 * @version 2019/11/28
 * @since JDK1.8
 */
@Slf4j
public class ClassInstanceHandler {

    private List<String> classNamesList = new ArrayList<>();

    public Map<String, Object> instanceMap = new HashMap<>();
    public static Map<String, Method> handlerMapping = new HashMap<>();
    public static Map<String, Object> controllerMap = new HashMap<>();

    /**
     * 获取模块下所有的class
     */
    public ClassInstanceHandler getModuleClass(String packageName) {
        URL url = this.getClass().getResource("/com/netty/server/module");
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                //递归读取包
                String str = packageName +"/"+ file.getName();
                System.out.println(str);
                getModuleClass(str);
            } else {
                String className = packageName + "." + file.getName().replace(".class", "");
                classNamesList.add(className);
            }
        }
        return this;
    }

    /**
     * 实例化
     */
    public ClassInstanceHandler getNeedInstanceClass() {
        if (!classNamesList.isEmpty()) {
            for (String className : classNamesList) {
                try {
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isAnnotationPresent(NettyController.class)) {
                        instanceMap.put(clazz.getSimpleName(), clazz.newInstance());
                    } else {
                        continue;
                    }
                } catch (Exception e) {
                    log.error("实例化[{}]失败",className);
                    e.printStackTrace();
                    continue;
                }
            }
        }
        return this;
    }

    /**
     * 绑定url和具体方法
     */
    public void initHandlerMapping() {
        instanceMap.forEach((k, v) -> {
            Class<? extends Object> clazz = v.getClass();
            if (clazz.isAnnotationPresent(NettyController.class)) {
                String baseUrl = "";
                if (clazz.isAnnotationPresent(NettyRequestMapping.class)) {
                    NettyRequestMapping annotation = clazz.getAnnotation(NettyRequestMapping.class);
                    baseUrl = annotation.value();
                }
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (!method.isAnnotationPresent(NettyRequestMapping.class)) {
                        continue;
                    }
                    NettyRequestMapping annotation = method.getAnnotation(NettyRequestMapping.class);
                    String url = annotation.value();

                    url = (baseUrl + "/" + url).replaceAll("/+", "/");
                    handlerMapping.put(url, method);
                    try {
                        controllerMap.put(url, clazz.newInstance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        });

    }
}