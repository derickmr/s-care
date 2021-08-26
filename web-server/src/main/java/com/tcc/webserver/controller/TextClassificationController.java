package com.tcc.webserver.controller;

import com.tcc.webserver.dto.TextClassificationData;
import com.tcc.webserver.service.ClassificationService;
import lombok.Getter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Getter
@RestController
@RequestMapping(value = "/classification")
public class TextClassificationController {

    @Resource
    private ClassificationService classificationService;

    @PostMapping("/text")
    public TextClassificationData classifyText(@RequestBody TextClassificationData classificationData) {

        return this.getClassificationService().classifyTextAndCreateContext(classificationData);

    }

}
