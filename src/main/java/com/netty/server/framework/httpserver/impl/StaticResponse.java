/**
 * Copyright (C), 2015-2019
 * FileName: StaticResponse
 * Project Name: netty-http-server
 * Date:     2019/11/28 15:10
 * Description:
 */
package com.netty.server.framework.httpserver.impl;

import com.netty.server.framework.constants.MimeType;
import com.netty.server.framework.httpserver.HttpServerResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URI;

import static com.netty.server.framework.constants.SysConstants.BASE_PATH;

/**
 * 〈一句话功能简述〉
 * 〈〉
 *
 * @author mashuai
 * @version 2019/11/28
 * @since JDK1.8
 */
public class StaticResponse implements HttpServerResponse {

    @Override
    public void response(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest, HttpResponseStatus code, String context) throws Exception {
        URI uri = new URI(fullHttpRequest.uri());
        String uriPath = uri.getPath();
        String path = BASE_PATH + uriPath;
        File rfile = new File(path);
        if (HttpUtil.is100ContinueExpected(fullHttpRequest)) {
            send100Continue(ctx);
        }
        String mimeType = MimeType.getMimeType(MimeType.parseSuffix(path));
        long length = 0;
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(rfile, "r");
            length = raf.length();
        } finally {
            if (length < 0 && raf != null) {
                raf.close();
            }
        }
        HttpResponse response = new DefaultHttpResponse(fullHttpRequest.protocolVersion(), code);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, mimeType);
        boolean keepAlive = HttpUtil.isKeepAlive(fullHttpRequest);
        if (keepAlive) {
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, length);
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        ctx.write(response);

        if (ctx.pipeline().get(SslHandler.class) == null) {
            ctx.write(new DefaultFileRegion(raf.getChannel(), 0, length));
        } else {
            ctx.write(new ChunkedNioFile(raf.getChannel()));
        }

        ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }

    }

    private void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }
}