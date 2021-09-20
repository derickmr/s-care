package com.tcc.datasimulator.strategy.impl;

import com.tcc.datasimulator.data.*;
import com.tcc.datasimulator.service.FileService;
import com.tcc.datasimulator.service.impl.DefaultFileService;
import com.tcc.datasimulator.strategy.UserScenarioStrategy;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public abstract class AbstractUserScenarioStrategy implements UserScenarioStrategy {

    @Resource
    private FileService fileService;

    @Value("${tcc.s-care.endpoint}")
    private String sCareEndpoint;

    @SneakyThrows
    @Override
    public void run(int quantity) {

        List<ClassificationEntry> allEntries = fileService.parseClassificationFile();

        List<ClassificationEntry> filteredEntries = getFilteredEntries(quantity, allEntries);

        processFilteredEntries(filteredEntries);
    }

    @SneakyThrows
    @Override
    public void run(int quantity, Long userId) {

        List<ClassificationEntry> allEntries = fileService.parseClassificationFile();

        List<ClassificationEntry> filteredEntries = getFilteredEntries(quantity, allEntries);

        processFilteredEntries(filteredEntries, userId);
    }

    public void processFilteredEntries(List<ClassificationEntry> entries, Long userId) {

        final List<TextClassificationData> results = entries.stream().map(
                entry -> {
                    TextClassificationData requestBody = createRequestBody(entry);
                    requestBody.setUserId(userId);
                    final ResponseEntity<TextClassificationData> response = restTemplate().postForEntity(getTextClassificationUrl(), requestBody, TextClassificationData.class);
                    return response.getBody();
                }
        ).collect(Collectors.toList());

        System.out.println(results);

    }

    public void processFilteredEntries(List<ClassificationEntry> entries) {

        final List<TextClassificationData> results = entries.stream().map(
                entry -> {
                    TextClassificationData requestBody = createRequestBody(entry);
                    requestBody.setUserId(createUser());
                    final ResponseEntity<TextClassificationData> response = restTemplate().postForEntity(getTextClassificationUrl(), requestBody, TextClassificationData.class);
                    return response.getBody();
                }
        ).collect(Collectors.toList());

        System.out.println(results);

    }

    protected String getTextClassificationUrl() {
        return sCareEndpoint + "/classification/text";
    }

    protected TextClassificationData createRequestBody(ClassificationEntry entry) {
        TextClassificationData requestBody = new TextClassificationData();

        Location location = new Location();
        location.setDate(new Date());
        location.setSemanticLocation("Home");
        location.setGeographicalLocation(new GeographicalLocation("123123123", "321321321"));

        requestBody.setText(entry.getText());
        requestBody.setDate(new Date());
        requestBody.setLocation(location);

        return requestBody;
    }

    protected Long createUser() {

        User user = new User();
        AlarmContact alarmContact = new AlarmContact();

        user.setName("test");
        alarmContact.setEmail("derickmartinss1@gmail.com");
        user.setAlarmContacts(Collections.singletonList(alarmContact));

        ResponseEntity<User> result = restTemplate().postForEntity(getCreateUserEndpoint(), user, User.class);

        return result.getBody().getId();

    }

    private String getCreateUserEndpoint() {
        return sCareEndpoint + "/user";
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

    @Bean
    private RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
