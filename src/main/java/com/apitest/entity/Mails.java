package com.apitest.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Mails implements Serializable {

    private Integer id;

    private Long createTime;

    private String content;

    private String subject;

    public Mails() {
        if (this.getCreateTime() == null) {
            this.setCreateTime(System.currentTimeMillis() / 1000);
        }
    }
}
