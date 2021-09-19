package com.tcc.datasimulator.strategy.impl;

import com.tcc.datasimulator.data.ClassificationEntry;
import com.tcc.datasimulator.strategy.UserScenarioStrategy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component("mixedRiskStrategy")
public class DefaultMixedRiskStrategy extends AbstractUserScenarioStrategy {

    private AbstractUserScenarioStrategy userAtRiskStrategy = new DefaultUserAtRiskStrategy();

    private AbstractUserScenarioStrategy userAtNoRiskStrategy = new DefaultUserAtNoRiskStrategy();

    @Override
    public List<ClassificationEntry> getFilteredEntries(int quantity, List<ClassificationEntry> entries) {

        int riskQuantity = quantity / 2;

        if (quantity % 2 != 0) {
            if (Math.random() < 0.5) {
                riskQuantity++;
            }
        }

        int noRiskQuantity = quantity - riskQuantity;

        List<ClassificationEntry> result = new ArrayList<>();

        result.addAll(userAtRiskStrategy.getFilteredEntries(riskQuantity, entries));
        result.addAll(userAtNoRiskStrategy.getFilteredEntries(noRiskQuantity, entries));

        Collections.shuffle(result);
        Collections.shuffle(result);
        Collections.shuffle(result);

        return result;
    }

}
