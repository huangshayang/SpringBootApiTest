package com.apitest.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
public class Apis implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String url;

    @Column
    private Timestamp createTime;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private Integer envId;

    @Column
    private Timestamp updateTime;

    @Column(nullable = false)
    private Boolean cookie;

    @Column(nullable = false, unique = true)
    private String name;

    private Apis() {
        if (this.getCreateTime() == null) {
            this.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        if (this.getUpdateTime() == null) {
            this.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        }
    }
}
