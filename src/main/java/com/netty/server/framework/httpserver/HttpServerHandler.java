/**
 * Copyright (C), 2015-2019
 * FileName: HttpServerHandler
 * Project Name: netty-study
 * Date:     2019/11/27 9:52
 * Description:
 */
package com.netty.server.framework.httpserver;

import com.netty.server.framework.constants.SysConstants;
import com.netty.server.framework.httpserver.impl.CommonResponse;
import com.netty.server.framework.httpserver.impl.DataResponse;
import com.netty.server.framework.httpserver.impl.StaticResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.File;
import java.util.Arrays;

import static com.netty.server.framework.constants.SysConstants.STATIC_SOURCE;


/**
 * 〈一句话功能简述〉
 * 〈〉
 *
 * @author mashuai
 * @version 2019/11/27
 * @since JDK1.8
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) throws Exception {
        /**
         * 根据请求后缀判断是否是静态资源
         * G:\my-work-code\MyWorkSpace\netty-http-server\src\webapp\img
         */
        if (checkIsStaticResource(fullHttpRequest.uri())) {
            File file = new File(SysConstants.BASE_PATH + fullHttpRequest.uri());
            if (file.exists()) {
                new StaticResponse().response(ctx, fullHttpRequest, HttpResponseStatus.OK, "");
            } else {
                new CommonResponse().response(ctx, fullHttpRequest, HttpResponseStatus.NOT_FOUND, "File not found");
            }
        } else {  //请求数据
            new DataResponse().response(ctx, fullHttpRequest, HttpResponseStatus.NOT_FOUND, "File not found");

        }
    }
    private boolean checkIsStaticResource(String requestUri) {
        return Arrays.stream(STATIC_SOURCE).filter(it -> requestUri.endsWith(it)).count() > 0;
    }

}