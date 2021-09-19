package com.tcc.datasimulator.data;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({"text", "cls"})
public class ClassificationEntry {

    private String text;
    private String classification;

}
