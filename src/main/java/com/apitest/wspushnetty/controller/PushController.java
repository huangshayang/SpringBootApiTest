package com.apitest.wspushnetty.controller;

import com.apitest.wspushnetty.produce.PushProduceFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/push")
public class PushController {

    private final AmqpTemplate amqpTemplate;
    private static boolean flag = true;

    @Autowired
    public PushController(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    @PostMapping(path = "/startClw")
    public void pushStart(@RequestParam(name = "type")String type) throws InterruptedException {
        switch (type) {
            case "S6":
                while (flag) {
                    amqpTemplate.convertAndSend(type, new PushProduceFactory().push(type));
                    Thread.sleep(1000);
                }
                break;
            case "S7":
                while (flag) {
                    //                amqpTemplate.convertAndSend(type, new PushProduceFactory().push(type));
//                Thread.sleep(1000);
                }
                break;
        }

    }

    @DeleteMapping(path = "/stopClw")
    public void pushStop(@RequestParam(name = "type")String type) {
        switch (type) {
            case "S6":
                flag = false;
                break;
            case "S7":
                flag = false;
                break;
        }

    }
}
