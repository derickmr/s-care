package com.tcc.datasimulator.strategy;


import com.tcc.datasimulator.data.TextClassificationData;
import lombok.SneakyThrows;

import java.util.List;

public interface UserScenarioStrategy {

    @SneakyThrows
    List<TextClassificationData> run(int quantity);

    @SneakyThrows
    List<TextClassificationData> run(int quantity, Long userId);
}
