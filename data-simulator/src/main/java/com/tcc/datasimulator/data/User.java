package com.tcc.datasimulator.data;

import lombok.Data;

import java.util.List;

@Data
public class User {

    private Long id;
    private String name;
    private List<AlarmContact> alarmContacts;

}
