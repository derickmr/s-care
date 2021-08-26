package com.tcc.webserver.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class AlarmContact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;
    private String phoneNumber;

    @ManyToOne
    private User user;

}
