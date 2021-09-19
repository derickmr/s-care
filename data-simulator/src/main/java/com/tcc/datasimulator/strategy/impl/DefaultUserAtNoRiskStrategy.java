package com.tcc.datasimulator.strategy.impl;

import com.tcc.datasimulator.data.ClassificationEntry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("userAtNoRiskStrategy")
public class DefaultUserAtNoRiskStrategy extends AbstractUserScenarioStrategy{

    @Override
    public List<ClassificationEntry> getFilteredEntries(int quantity, List<ClassificationEntry> entries) {
        final List<ClassificationEntry> riskyEntries = entries.stream().filter(
                entry -> entry.getClassification().equalsIgnoreCase("No Risk")
        ).collect(Collectors.toList());

        return getEntriesRandomly(quantity, riskyEntries);
    }

}
