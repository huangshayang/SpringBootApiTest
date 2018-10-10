package com.apitest.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author huangshayang
 */
@Entity
@Data
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;
}
