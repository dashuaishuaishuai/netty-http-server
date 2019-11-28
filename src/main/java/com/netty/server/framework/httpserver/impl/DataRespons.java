/**
 * Copyright (C), 2015-2019
 * FileName: DataRespons
 * Project Name: netty-http-server
 * Date:     2019/11/28 15:22
 * Description:
 */
package com.netty.server.framework.httpserver.impl;

import com.netty.server.framework.httpserver.HttpServerResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.netty.server.framework.instance.ClassInstanceHandler.handlerMapping;
import static com.netty.server.framework.instance.ClassInstanceHandler.controllerMap;

/**
 * 〈一句话功能简述〉
 * 〈〉
 *
 * @author mashuai
 * @version 2019/11/28
 * @since JDK1.8
 */
public class DataRespons implements HttpServerResponse {

    @Override
    public void response(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest, HttpResponseStatus code, String context) throws Exception {
        if (handlerMapping.isEmpty()) {
            return;
        }
        String realUrl = getRealUrl(fullHttpRequest);
        if (handlerMapping.containsKey(realUrl)) {
            Map<String, Object> requestParams = getRequestParams(fullHttpRequest);
            Method method = handlerMapping.get(realUrl);
            Parameter[] params = method.getParameters();
            Object[] paramValues = Arrays.stream(params).map(it -> requestParams.get(it)).collect(Collectors.toList()).toArray();
            try {
                Object obj = method.invoke(controllerMap.get(realUrl), paramValues);
                System.out.println(obj);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

        }
    }

    /**
     * 获取请求url
     *
     * @param fullHttpRequest
     * @return
     */
    private String getRealUrl(FullHttpRequest fullHttpRequest) {
        QueryStringDecoder decoder = new QueryStringDecoder(fullHttpRequest.uri());
        return decoder.path();
    }

    /**
     * 获取请求参数
     *
     * @return
     */
    private Map<String, Object> getRequestParams(FullHttpRequest request) throws IOException {
        HttpMethod method = request.method();
        Map<String, Object> parmMap = new HashMap<>();
        if (HttpMethod.GET == method) {
            QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
            decoder.parameters().entrySet().forEach(entry -> {
                parmMap.put(entry.getKey(), entry.getValue().get(0));
            });
        } else if (HttpMethod.POST == method) {
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
            decoder.offer(request);
            List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();
            for (InterfaceHttpData parm : parmList) {
                Attribute data = (Attribute) parm;
                parmMap.put(data.getName(), data.getValue());
            }
        }
        return parmMap;
    }

}