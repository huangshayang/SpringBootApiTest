package com.apitest.entity;


import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(indexes = @Index(name = "index_api_id", columnList = "apiId"))
public class Cases {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @Lob
    private String jsonData;

    @Column(nullable = false)
    private String paramsData;

    @Column
    private String contentType;

    @Column
    @Lob
    private String expectResult;

    @Column
    private Timestamp createTime;

    @Column
    private Timestamp updateTime;

    @Column(nullable = false)
    private Integer apiId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean available;

    private Cases(){
        if (this.getCreateTime() == null) {
            this.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        if (this.getUpdateTime() == null) {
            this.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        }
        if (jsonData == null) {
            this.jsonData = "";
        }
        if (paramsData == null) {
            this.paramsData = "";
        }
    }
}
