package com.apitest.entity;


import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
public class Apis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private Date createTime;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private Date updateTime;

    @Column(nullable = false)
    private Boolean cookie;

    @Column(nullable = false)
    private String note;
}
