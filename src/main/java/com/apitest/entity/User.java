package com.apitest.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @author huangshayang
 */
@Data
//必须实现序列化，才能存储到redis
public class User implements Serializable {

    @JsonIgnore
    private Integer id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private Long createTime;

    @JsonIgnore
    private Long updateTime;

    public User() {
        if (this.getCreateTime() == null) {
            this.setCreateTime(System.currentTimeMillis() / 1000);
        }
        if (this.getUpdateTime() == null) {
            this.setUpdateTime(System.currentTimeMillis() / 1000);
        }
    }
}
