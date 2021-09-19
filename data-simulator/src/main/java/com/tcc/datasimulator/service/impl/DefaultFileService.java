package com.tcc.datasimulator.service.impl;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.tcc.datasimulator.data.ClassificationEntry;
import com.tcc.datasimulator.service.FileService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class DefaultFileService implements FileService {

    @Override
    public List<ClassificationEntry> parseClassificationFile() throws IOException {

        File csvFile = new File("src/main/resources/files/gold_reddit_corpus_agree.csv");

        MappingIterator<ClassificationEntry> classificationsIterator = new CsvMapper().readerWithTypedSchemaFor(ClassificationEntry.class).readValues(csvFile);
        List<ClassificationEntry> classifications = classificationsIterator.readAll();

        removeFileHeader(classifications);

        return classifications;
    }

    /**
     * Removes the file header, which is "text | cls"
     * @param classifications
     */
    private void removeFileHeader(List<ClassificationEntry> classifications) {
        classifications.remove(0);
    }
}
