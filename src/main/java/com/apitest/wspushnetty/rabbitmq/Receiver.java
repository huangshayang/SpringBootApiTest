package com.apitest.wspushnetty.rabbitmq;

import com.alibaba.fastjson.JSONObject;
import com.apitest.wspushnetty.config.SocketServer;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = {"S6", "S7"}, containerFactory = "customContainerFactory")
public class Receiver {

    private final SocketServer socketServer;

    @Autowired
    public Receiver(SocketServer socketServer) {
        this.socketServer = socketServer;
    }

    @RabbitHandler
    public void receiver(JSONObject message){
        System.out.println("消费线程: " + Thread.currentThread().getName() + ", Push内容: " + message);
        socketServer.sendMessageToClient(message);
    }
}
