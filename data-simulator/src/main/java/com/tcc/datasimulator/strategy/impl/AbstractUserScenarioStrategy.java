package com.tcc.datasimulator.strategy.impl;

import com.tcc.datasimulator.data.ClassificationEntry;
import com.tcc.datasimulator.service.FileService;
import com.tcc.datasimulator.service.impl.DefaultFileService;
import com.tcc.datasimulator.strategy.UserScenarioStrategy;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public abstract class AbstractUserScenarioStrategy implements UserScenarioStrategy {

    @Resource
    private FileService fileService;

    @SneakyThrows
    public void run(int quantity) {

        if (fileService == null) {
            fileService = new DefaultFileService();
        }

        List<ClassificationEntry> allEntries = fileService.parseClassificationFile();

        List<ClassificationEntry> filteredEntries = getFilteredEntries(quantity, allEntries);

        processFilteredEntries(filteredEntries);

    }

    public void processFilteredEntries(List<ClassificationEntry> entries) {
        //TODO call s-care to classify entries
    }

    public abstract List<ClassificationEntry> getFilteredEntries(int quantity, List<ClassificationEntry> entries);

    protected List<ClassificationEntry> getEntriesRandomly(int quantity, List<ClassificationEntry> entries) {

        List<ClassificationEntry> result = new ArrayList<>();

        int upperBound = entries.size();
        Random random = new Random();

        for (int i = 0; i < quantity; i++) {
            result.add(entries.get(random.nextInt(upperBound)));
        }

        return result;
    }

}
