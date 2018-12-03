package com.apitest.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(indexes = @Index(name = "index_api_id", columnList = "apiId"))
public class Logs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp requestTime;

    @Column(nullable = false)
    @Lob
    private String responseData;

    @Column(nullable = false)
    @Lob
    private String requestData;

    @Column(nullable = false)
    @Lob
    private String responseHeader;

    @Column(nullable = false)
    private Integer code;

    @Column(nullable = false)
    private Integer apiId;

    @Column(nullable = false)
    private String note;
}
