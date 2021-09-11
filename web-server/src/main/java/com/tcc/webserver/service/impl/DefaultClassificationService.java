package com.tcc.webserver.service.impl;

import com.tcc.webserver.dto.TextClassificationData;
import com.tcc.webserver.dto.TextClassificationRequest;
import com.tcc.webserver.models.Context;
import com.tcc.webserver.models.User;
import com.tcc.webserver.repository.ContextRepository;
import com.tcc.webserver.repository.UserRepository;
import com.tcc.webserver.service.ClassificationService;
import com.tcc.webserver.service.NotificationService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
@Getter
public class DefaultClassificationService implements ClassificationService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private ContextRepository contextRepository;

    @Resource
    private NotificationService notificationService;

    @Value("${tcc.text-classifier-endpoint}")
    private String classifierEndpoint;

    @Override
    public TextClassificationData classifyTextAndCreateContext(TextClassificationData textClassificationData) {

        final String response = this.getClassificationForText(textClassificationData.getText());

        textClassificationData.setClassification(this.getClassificationFromResponse(response));
        textClassificationData.setProbability(this.getProbabilityFromResponse(response));

        this.createContext(textClassificationData);

        final Long userId = textClassificationData.getUserId();
        final User user = this.getUserRepository().getById(userId);

        if (notificationService.shouldSendNotificationForUser(user)) {
            notificationService.sendEmailNotificationForUser(user);
        }

        return textClassificationData;
    }

    @Override
    public int getFlagForClassificationProbability(double probability) {

        if (probability >= 95) {
            return 4;
        }

        if (probability >= 85) {
            return 3;
        }

        if (probability >= 75) {
            return 2;
        }

        return 1;
    }

    protected void createContext(TextClassificationData textClassificationData) {

        final Context context = new Context();

        context.setText(textClassificationData.getText());
        context.setClassification(textClassificationData.getClassification());
        context.setProbability(textClassificationData.getProbability());
        context.setUser(this.getUserRepository().getById(textClassificationData.getUserId()));
        context.setDate(textClassificationData.getDate());
        context.setLocation(textClassificationData.getLocation());
        context.setRiskFlag(this.getFlagForClassificationProbability(context.getProbability()));

        this.getContextRepository().save(context);
    }

    protected String getClassificationForText(final String text) {
        TextClassificationRequest requestBody = new TextClassificationRequest();
        requestBody.setText(text);

        final ResponseEntity<String> response = this.restTemplate().postForEntity(this.getClassifierEndpoint(), requestBody, String.class);

        return response.getBody();
    }

    protected String getClassificationFromResponse(final String response) {
        return response.split(": ")[0];
    }

    protected Double getProbabilityFromResponse(final String response) {
        return Double.parseDouble(response.split(": ")[1]) * 100;
    }

    @Bean
    private RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
