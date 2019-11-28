/**
 * Copyright (C), 2015-2019
 * FileName: HttpServerResponse
 * Project Name: netty-http-server
 * Date:     2019/11/28 15:06
 * Description:
 */
package com.netty.server.framework.httpserver;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

/**
 * 〈一句话功能简述〉
 * 〈〉
 *
 * @author mashuai
 * @version 2019/11/28
 * @since JDK1.8
 */
public interface HttpServerResponse {

    /**
     *
     * @param ctx
     * @param request
     * @param code
     * @param context
     * @throws Exception
     */
    default void response(ChannelHandlerContext ctx, FullHttpRequest request, HttpResponseStatus code, String context) throws Exception {
        FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, code, Unpooled.copiedBuffer(context.getBytes("UTF-8")));
        fullHttpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html");
        fullHttpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, fullHttpResponse.content().readableBytes());
        ChannelFuture future = ctx.writeAndFlush(fullHttpResponse);
        if (!HttpUtil.isKeepAlive(request)) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

}