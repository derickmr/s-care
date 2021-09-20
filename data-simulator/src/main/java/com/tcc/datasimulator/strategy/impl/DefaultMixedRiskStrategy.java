package com.tcc.datasimulator.strategy.impl;

import com.tcc.datasimulator.data.ClassificationEntry;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component("mixedRiskStrategy")
public class DefaultMixedRiskStrategy extends AbstractUserScenarioStrategy {

    @Resource
    private AbstractUserScenarioStrategy userAtRiskStrategy;

    @Resource
    private AbstractUserScenarioStrategy userAtNoRiskStrategy;

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
