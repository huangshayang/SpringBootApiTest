package com.apitest.wspushnetty.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SockerRunner implements CommandLineRunner {

    private final SocketIOServer server;

    @Autowired
    public SockerRunner(SocketIOServer server) {
        this.server = server;
    }

    @Override
    public void run(String... args) {
        // 启动socket服务
        server.start();
    }
}
