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
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

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

    private static final int BLACK_FLAG = 4;
    private static final int RED_FLAG = 3;
    private static final int ORANGE_FLAG = 2;
    private static final int YELLOW_FLAG = 1;
    private static final int NO_RISK_FLAG = 0;

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

    /**
     * Possíveis bandeiras de certeza de risco:
     * Preta - Certeza > 95%
     * Vermelha - Certeza > 85%
     * Laranja - Certeza > 75%
     * Amarela - Certeza > 50%
     *
     * @param probability
     * @return
     */
    @Override
    public int getFlagForClassificationProbability(double probability) {

        if (probability >= 95) {
            return BLACK_FLAG;
        }

        if (probability >= 85) {
            return RED_FLAG;
        }

        if (probability >= 75) {
            return ORANGE_FLAG;
        }

        return YELLOW_FLAG;
    }

    /**
     * Cenários para enviar a notificação:
     * 1 - A classificação do último contexto é uma bandeira preta (certeza > 95%)
     * 2 - O usuário apresentou uma piora de qualquer bandeira para a bandeira vermelha (certeza > 85%)
     * 3 - A classificação do último contexto é uma bandeira vermelha (certeza > 85%) e usuário apresentou outra bandeira vermelha nos últimos 5 contextos (exemplo: preta -> vermelha -> vermelha)
     * 4 - O usuário apresentou uma piora de pelo menos 2 bandeiras (exemplo, verde -> laranja)
     * 5 - A media das ultimas 5 bandeiras foi acima de 80%
     *
     * @param contexts
     * @return
     */
    @Override
    public boolean isUserAtRisk(List<Context> contexts) {

        Context currentContext = contexts.iterator().next();
        List<Context> previousContexts = contexts.subList(1, contexts.size());

        if (contextIsBlackFlag(currentContext)) {
            return true;
        }

        if (hasWorsenToRedFlag(currentContext, previousContexts)) {
            return true;
        }

        if (containsMultipleRedFlags(currentContext, previousContexts)) {
            return true;
        }

        if (hasPresentedWorsenOfAtLeastTwoFlags(currentContext, previousContexts)) {
            return true;
        }

        return getProbabilityAverage(contexts) > 80;
    }

    private boolean containsMultipleRedFlags(Context currentContext, List<Context> previousContexts) {
        return isRedFlag(currentContext) && containsRedFlag(previousContexts);
    }

    private Double getProbabilityAverage(List<Context> contexts) {
        return (contexts.stream().mapToDouble(Context::getProbability).sum()) / contexts.size();
    }

    private boolean containsRedFlag(List<Context> contexts) {
        return contexts.stream().anyMatch(
                this::isRedFlag
        );
    }

    private boolean isRedFlag(Context context) {
        return context.getProbability() == RED_FLAG;
    }

    private boolean hasPresentedWorsenOfAtLeastTwoFlags(Context currentContext, List<Context> previousContexts) {
        return (currentContext.getProbability() - this.getContextWithLowestProbability(previousContexts).getProbability()) >= 2;
    }

    private Context getContextWithLowestProbability(List<Context> contexts) {
        return contexts.stream().reduce((i, j) -> i.getProbability() < j.getProbability() ? i : j).get();
    }

    private boolean hasWorsenToRedFlag(Context currentContext, List<Context> previousContexts) {

        if (CollectionUtils.isEmpty(previousContexts)) {
            return false;
        }

        return currentContext.getRiskFlag() == RED_FLAG && previousContexts.iterator().next().getRiskFlag() < RED_FLAG;
    }

    private boolean contextIsBlackFlag(Context context) {
        return context.getRiskFlag() == BLACK_FLAG;
    }

    protected void createContext(TextClassificationData textClassificationData) {

        final Context context = new Context();

        context.setText(textClassificationData.getText());
        context.setClassification(textClassificationData.getClassification());
        context.setProbability(textClassificationData.getProbability());
        context.setUser(this.getUserRepository().getById(textClassificationData.getUserId()));
        context.setDate(textClassificationData.getDate());
        context.setLocation(textClassificationData.getLocation());

        if (context.getClassification().equalsIgnoreCase("Risk")) {
            context.setRiskFlag(this.getFlagForClassificationProbability(context.getProbability()));
        } else {
            context.setRiskFlag(NO_RISK_FLAG);
        }

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
