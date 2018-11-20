package com.apitest.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author huangshayang
 */
@Entity
@Data
//必须实现序列化，才能存储到redis
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column
    @JsonIgnore
    private Timestamp createTime;

    @Column
    @JsonIgnore
    private Timestamp updateTime;

    @Column(nullable = false)
    private String uid;

    public User(){
        if (this.getCreateTime() == null) {
            this.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        if (this.getUpdateTime() == null) {
            this.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        }
        if (this.getUid() == null) {
            this.setUid(UUID.randomUUID().toString().replace("-", ""));
        }
    }
}
