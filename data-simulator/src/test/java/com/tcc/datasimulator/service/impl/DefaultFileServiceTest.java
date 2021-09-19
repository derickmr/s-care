package com.tcc.datasimulator.service.impl;

import com.tcc.datasimulator.data.ClassificationEntry;
import com.tcc.datasimulator.service.FileService;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultFileServiceTest {

    @Test
    public void testFileLoading() throws IOException {

        final FileService fileService = new DefaultFileService();

        final List<ClassificationEntry> result = fileService.parseClassificationFile();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(result.size(), 273);

    }

}