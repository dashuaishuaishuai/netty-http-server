/**
 * Copyright (C), 2015-2019
 * FileName: HttpServerContainer
 * Project Name: netty-http-server
 * Date:     2019/11/27 17:00
 * Description:
 */
package com.netty.server.framework.httpserver;

import com.netty.server.Container;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;


/**
 * 〈一句话功能简述〉
 * 〈〉
 *
 * @author mashuai
 * @version 2019/11/27
 * @since JDK1.8
 */
@Slf4j
public class HttpServerContainer implements Container {

    EventLoopGroup bossGrop = new NioEventLoopGroup();
    EventLoopGroup workGrop = new NioEventLoopGroup();

    @Override
    public void start(String... args) {
        int port = 8080;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception e) {
                log.error("获取端口失败,绑定默认端口8080");
            }
        }
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGrop, workGrop)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sh) throws Exception {
                            sh.pipeline().addLast(new HttpServerCodec());
                            sh.pipeline().addLast(new HttpObjectAggregator(64 * 1024));
                            sh.pipeline().addLast(new ChunkedWriteHandler());
                            sh.pipeline().addLast(new HttpServerHandler());

                        }
                    });

            ChannelFuture f = b.bind(port);
            log.info("服务器启动成功.......");
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop(String... arg) {
        log.debug("服务器停止中.......");
        bossGrop.shutdownGracefully();
        workGrop.shutdownGracefully();
    }
}