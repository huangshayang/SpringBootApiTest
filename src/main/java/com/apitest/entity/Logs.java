package com.apitest.entity;


import lombok.Data;

import java.io.Serializable;

@Data
public class Logs implements Serializable {

    private Integer id;

    private Long requestTime;

    private String responseBody;

    private String responseHeader;

    private Integer code;

    private Integer apiId;

    private String caseName;

    private Integer checkBoolean;

    private String errorMsg;

}
