package com.apitest.entity;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
public class Logs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Timestamp request_time;

    @Column(nullable = false)
    @Lob
    private String response_data;

    @Column(nullable = false)
    @Lob
    private String request_data;

    @Column(nullable = false)
    @Lob
    private String response_header;

    private BigDecimal response_time;

    @Column(nullable = false)
    private Integer code;

    @Column(nullable = false)
    private Integer apiId;

    @Column(nullable = false)
    private String note;
}
