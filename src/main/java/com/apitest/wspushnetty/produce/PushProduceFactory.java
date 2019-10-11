package com.apitest.wspushnetty.produce;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.apitest.wspushnetty.pojo.LocationPojo;

import java.util.Random;

public class PushProduceFactory {

    private String packetType;

    public JSONObject push(String packetType) {
        this.packetType = packetType;
        JSONObject jsonObject = new JSONObject();
        switch (packetType) {
            case "S6":
                jsonObject = s6Produce();
                break;
            case "S7":
                jsonObject = s7Produce();
        }
        return jsonObject;
    }

    private JSONObject s6Produce() {
        JSONObject pushObject = new JSONObject();
        JSONArray resArray = new JSONArray();
        JSONObject resObject = new JSONObject();
        packetType = "S6";
        int alt = new Random().nextInt(500);
        int degree = new Random().nextInt(361);
        double lat = Math.random()+41;
        double lng = Math.random()+113;
        long time = System.currentTimeMillis() / 1000;
        int speed = new Random().nextInt(100);
        resObject.put("location", new LocationPojo(alt, degree, lat, lng, time, speed));
        resObject.put("vehicle_id", new Random().nextInt(4069) + 46);
        resArray.add(resObject);
        pushObject.put("packet_type", packetType);
        pushObject.put("res", resArray);
        return pushObject;
    }

    private JSONObject s7Produce() {
        packetType = "S7";
        return new JSONObject();
    }

}
