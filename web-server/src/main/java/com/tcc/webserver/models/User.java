package com.tcc.webserver.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany
    private List<AlarmContact> alarmContacts;

    @OneToMany
    private List<Context> contexts;

}

