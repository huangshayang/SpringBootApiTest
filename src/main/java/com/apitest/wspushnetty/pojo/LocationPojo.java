package com.apitest.wspushnetty.pojo;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode
@JSONType
public class LocationPojo implements Serializable {

    private int altitude;
    private int degree;
    private double lat;
    private double lng;
    private long locateTime;
    private int speed;

    public LocationPojo(int altitude, int degree, double lat, double lng, long locateTime, int speed) {
        this.altitude = altitude;
        this.degree = degree;
        this.lat = lat;
        this.lng = lng;
        this.locateTime = locateTime;
        this.speed = speed;
    }
}
