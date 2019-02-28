package com.apitest.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@Table(indexes = @Index(name = "index_api_id", columnList = "apiId"))
public class Logs implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Timestamp requestTime;

    @Column(nullable = false)
    @Lob
    private String responseData;

    @Column(nullable = false)
    @Lob
    private String jsonData;

    @Column(nullable = false)
    private String paramsData;

    @Column(nullable = false)
    @Lob
    private String responseHeader;

    @Column(nullable = false)
    private Integer code;

    @Column(nullable = false)
    private Integer apiId;

    @Column(nullable = false)
    private String caseName;

    public Logs() {
        if (jsonData == null) {
            this.jsonData = "";
        }
        if (paramsData == null) {
            this.paramsData = "";
        }
    }
}
