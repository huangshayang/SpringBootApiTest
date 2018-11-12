package com.apitest.entity;


import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@Table(indexes = @Index(name = "index_api_id", columnList = "apiId"))
public class Cases {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @Lob
    private String requestData;

    @Lob
    private String expectResult;

    @Column(nullable = false)
    private Date createTime;

    @Column(nullable = false)
    private Date updateTime;

    @Column(nullable = false)
    private Integer apiId;

    @Column(nullable = false)
    private String note;

    private Cases(){
        if (this.getCreateTime() == null) {
            this.setCreateTime(new Date(System.currentTimeMillis()));
        }
        if (this.getUpdateTime() == null) {
            this.setUpdateTime(new Date(System.currentTimeMillis()));
        }
    }
}
