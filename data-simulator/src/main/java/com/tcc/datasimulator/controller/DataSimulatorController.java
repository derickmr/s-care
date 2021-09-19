package com.tcc.datasimulator.controller;

import com.tcc.datasimulator.strategy.UserScenarioStrategy;
import com.tcc.datasimulator.strategy.impl.DefaultMixedRiskStrategy;
import com.tcc.datasimulator.strategy.impl.DefaultRandomRiskStrategy;
import com.tcc.datasimulator.strategy.impl.DefaultUserAtNoRiskStrategy;
import com.tcc.datasimulator.strategy.impl.DefaultUserAtRiskStrategy;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping(value = "/simulate/{mode}")
public class DataSimulatorController {

    private Map<String, UserScenarioStrategy> strategiesMap = Map.of(
            "risk", new DefaultUserAtRiskStrategy(),
            "noRisk", new DefaultUserAtNoRiskStrategy(),
            "mixed", new DefaultMixedRiskStrategy(),
            "random", new DefaultRandomRiskStrategy()
    );

    //TODO: include optional parameters (user id)
    @GetMapping
    public void simulate(@PathVariable String mode, @RequestParam int quantity) {

        strategiesMap.get(mode).run(quantity);

    }

}
