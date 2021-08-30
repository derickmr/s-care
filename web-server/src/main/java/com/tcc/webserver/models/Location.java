package com.tcc.webserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    @OneToOne
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    private GeographicalLocation geographicalLocation;

    private String semanticLocation;

}
