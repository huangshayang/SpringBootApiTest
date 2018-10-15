package com.apitest.entity;


import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Data
public class Apis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private Date create_time;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private Date update_time;

    @Column(nullable = false)
    private Boolean is_cookie;

    @Column(nullable = false)
    private String note;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "apiId")
    private List<Logs> logs;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "apiId")
    private List<Cases> cases;
}
