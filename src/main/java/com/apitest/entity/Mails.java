package com.apitest.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
public class Mails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @JsonIgnore
    private Timestamp createTime;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String subject;

    public Mails(){
        if (this.getCreateTime() == null) {
            this.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
    }
}
