package com.tcc.webserver.models;

import lombok.Data;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Context {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    private Date date;

    @Column(length = 50000)
    private String text;

    private String classification;
    private Double probability;
    private Integer riskFlag;

    @OneToOne
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    private Location location;

}
