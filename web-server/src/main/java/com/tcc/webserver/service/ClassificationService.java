package com.tcc.webserver.service;

import com.tcc.webserver.dto.TextClassificationData;

public interface ClassificationService {

    TextClassificationData classifyTextAndCreateContext (TextClassificationData textClassificationData);

}
