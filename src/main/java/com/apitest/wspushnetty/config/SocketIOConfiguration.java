package com.apitest.wspushnetty.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Transport;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOConfiguration {

    @Value("${netty.socket.address}")
    private String NettySocketAddress;

    @Value("${netty.socket.port}")
    private int NettySocketPort;

    @Value("${netty.socket.path}")
    private String NettySocketPath;

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration conf = new com.corundumstudio.socketio.Configuration();
        // 设置ip地址和端口
        conf.setHostname(NettySocketAddress);
        conf.setPort(NettySocketPort);
        conf.setContext(NettySocketPath);
        conf.setTransports(Transport.WEBSOCKET, Transport.POLLING);
        return new SocketIOServer(conf);
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketIOServer) {
        return new SpringAnnotationScanner(socketIOServer);
    }

}
