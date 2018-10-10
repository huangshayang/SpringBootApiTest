package com.apitest.entity;


import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
public class Cases {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @Lob
    private String request_data;

    @Lob
    private String expect_result;

    @Column(nullable = false)
    private Date create_time;

    @Column(nullable = false)
    private Date update_time;

    @Column(nullable = false)
    private Integer apiId;

    @Column(length = 50)
    private String note;

}
