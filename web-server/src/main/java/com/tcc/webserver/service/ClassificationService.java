package com.tcc.webserver.service;

import com.tcc.webserver.dto.TextClassificationData;
import com.tcc.webserver.models.Context;

import java.util.List;

public interface ClassificationService {

    TextClassificationData classifyTextAndCreateContext (TextClassificationData textClassificationData);

    int getFlagForClassificationProbability(double probability);

    boolean isUserAtRisk(List<Context> contexts, Context lastContext);
}
