package com.apitest.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Mails implements Serializable {

    private Integer id;

    private Timestamp createTime;

    private String content;

    private String subject;

    public Mails() {
        if (this.getCreateTime() == null) {
            this.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
    }
}
