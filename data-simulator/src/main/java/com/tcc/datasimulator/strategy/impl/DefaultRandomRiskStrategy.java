package com.tcc.datasimulator.strategy.impl;

import com.tcc.datasimulator.data.ClassificationEntry;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("randomRiskStrategy")
public class DefaultRandomRiskStrategy extends AbstractUserScenarioStrategy {

    @Override
    public List<ClassificationEntry> getFilteredEntries(int quantity, List<ClassificationEntry> entries) {
        return getEntriesRandomly(quantity, entries);
    }
}
