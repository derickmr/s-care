package com.tcc.datasimulator.data;

import lombok.Data;

import java.util.Date;

@Data
public class Location {

    private GeographicalLocation geographicalLocation;
    private String semanticLocation;
    private Date date;

}
