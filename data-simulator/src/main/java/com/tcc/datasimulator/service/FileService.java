package com.tcc.datasimulator.service;

import com.tcc.datasimulator.data.ClassificationEntry;

import java.io.IOException;
import java.util.List;

public interface FileService {

    List<ClassificationEntry> parseClassificationFile() throws IOException;

}
