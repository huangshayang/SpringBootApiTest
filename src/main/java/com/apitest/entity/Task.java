package com.apitest.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
public class Task {

    public enum TaskStatusEnum{
        //任务刚创建
        CREATED("创建"),
        //任务运行中
        RUNNING("运行"),
        //任务暂停中
        PAUSING("暂停"),
        //任务已停止
        STOPPED("停止");

        @Getter
        private String status;

        TaskStatusEnum(String status){
            this.status = status;
        }
    }

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
    @Enumerated(EnumType.STRING)
    private TaskStatusEnum taskStatus;

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
