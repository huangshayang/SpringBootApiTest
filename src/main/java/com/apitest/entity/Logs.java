package com.apitest.entity;


import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Logs implements Serializable {

    private Integer id;

    private Timestamp requestTime;

    private String responseData;

    private String responseHeader;

    private Integer code;

    private Integer apiId;

    private String caseName;

}
