package com.tcc.webserver.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TextClassificationData {

    private Long userId;
    private String text;
    private String classification;
    private Date date;
    private Double probability;

}
