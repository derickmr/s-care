package com.tcc.datasimulator.controller;

import com.tcc.datasimulator.data.TextClassificationData;
import com.tcc.datasimulator.strategy.UserScenarioStrategy;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/simulate/{mode}")
public class DataSimulatorController {

    @Resource
    private UserScenarioStrategy userAtRiskStrategy;

    @Resource
    private UserScenarioStrategy userAtNoRiskStrategy;

    @Resource
    private UserScenarioStrategy mixedRiskStrategy;

    @Resource
    private UserScenarioStrategy randomRiskStrategy;

    @GetMapping
    public List<TextClassificationData> simulate(@PathVariable String mode, @RequestParam int quantity, @RequestParam(required = false) Long userId) {
        UserScenarioStrategy strategy = this.getStrategiesMap().get(mode);

        if (userId != null) {
            return strategy.run(quantity, userId);
        } else {
            return strategy.run(quantity);
        }

    }

    private Map<String, UserScenarioStrategy> getStrategiesMap() {
        return Map.of(
                "risk", userAtRiskStrategy,
                "noRisk", userAtNoRiskStrategy,
                "mixed", mixedRiskStrategy,
                "random", randomRiskStrategy
        );
    }

}
