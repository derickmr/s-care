package com.tcc.datasimulator.strategy;


import lombok.SneakyThrows;

public interface UserScenarioStrategy {

    @SneakyThrows
    void run(int quantity);
}
