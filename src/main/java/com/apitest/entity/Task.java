package com.apitest.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Boolean isMultiThread;

    @Column(nullable = false)
    private String taskTime;

    @Column(nullable = false)
    @ManyToMany
    private List<Apis> apisList;

    @Column(nullable = false)
    private String taskStatus;

    @Column
    private Timestamp createTime;

    @Column
    private Timestamp updateTime;

    private Task(){
        if (this.getCreateTime() == null) {
            this.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        if (this.getUpdateTime() == null) {
            this.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        }
    }
}
