package com.apitest.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

@Data
public class Cases implements Serializable {

    private Integer id;

    private String jsonData;

    private String paramsData;

    @JsonIgnore
    private Long createTime;

    @JsonIgnore
    private Long updateTime;

    private Integer apiId;

    private String name;

    private Boolean available;

    private String expectResult;

    private Cases() {
        if (this.getCreateTime() == null) {
            this.setCreateTime(System.currentTimeMillis() / 1000);
        }
        if (this.getUpdateTime() == null) {
            this.setUpdateTime(System.currentTimeMillis() / 1000);
        }
        if (jsonData == null) {
            this.jsonData = "";
        }
        if (paramsData == null) {
            this.paramsData = "";
        }
    }
}
