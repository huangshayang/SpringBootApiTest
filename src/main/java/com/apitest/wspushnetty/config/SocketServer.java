package com.apitest.wspushnetty.config;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SocketServer {

    private final SocketIOServer socketServer;
    private static UUID uuid;

    @Value("${netty.socket.event}")
    private String NettySocketEvent;

    @Autowired
    public SocketServer(SocketIOServer socketServer) {
        this.socketServer = socketServer;
    }

    // 连接打开
    @OnConnect
    public void onConnect(SocketIOClient client) {
        System.out.println("socketio connect..");
        uuid = client.getSessionId();
    }

    // 连接关闭
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        System.out.println("socketio disconnect..");
        uuid = client.getSessionId();
    }

    public void sendMessageToClient(JSONObject message) {
        try {
            socketServer.getClient(uuid).sendEvent(NettySocketEvent, message);
        }catch (NullPointerException e) {
            System.out.println("客户端未连接");
        }
    }
}
