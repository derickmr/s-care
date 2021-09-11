package com.tcc.webserver.service.impl;

import com.tcc.webserver.models.*;
import com.tcc.webserver.service.ContextService;
import com.tcc.webserver.service.NotificationService;
import com.tcc.webserver.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultNotificationService implements NotificationService {

    private static final int BLACK_FLAG = 4;
    private static final int RED_FLAG = 3;
    private static final int ORANGE_FLAG = 2;
    private static final int YELLOW_FLAG = 1;
    private static final int MAXIMUM_THRESHOLD = 12;

    @Resource
    private ContextService contextService;

    @Resource
    private JavaMailSender emailSender;

    @Resource
    private UserService userService;

    @Override
    public boolean shouldSendNotificationForUser(User user) {
        final List<Context> contexts = this.contextService.getLastSubContextsFromUser(user, 5);
        final Context lastContext = this.contextService.getLastContextFromUser(user);

        if (lastContext == null) {
            return false;
        }

        return isUserAtRisk(contexts, lastContext);
    }

    @Override
    public void sendEmailNotificationForUser(User user) {
        final List<AlarmContact> contacts = user.getAlarmContacts().stream()
                .filter(contact -> StringUtils.isNotBlank(contact.getEmail()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(contacts)) {
            return;
        }

        contacts.forEach(
                contact -> this.emailSender.send(this.createEmailMessage(user, contact.getEmail()))
        );
    }

    protected SimpleMailMessage createEmailMessage(User user, String recipient) {
        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        final StringBuilder body = new StringBuilder();
        final Location lastKnownLocation = this.userService.getUserLastKnownLocation(user);
        final Context lastContext = this.contextService.getLastContextFromUser(user);

        body.append("S-Care system identified that the user ").append(user.getName()).append(" is at risk.");
        body.append("\n\n");
        body.append("The last message the user expressed was: ").append("\"").append(lastContext.getText()).append("\"").append(" at ").append(lastContext.getDate());
        body.append("\n");
        body.append("The probability of risk of this message is: ").append(lastContext.getProbability());

        if (lastKnownLocation != null) {
            final GeographicalLocation geographicalLocation = lastKnownLocation.getGeographicalLocation();

            body.append("\n\n");
            body.append("The last known location of the user is ").append(lastKnownLocation.getSemanticLocation())
                    .append(". Latitude: ").append(geographicalLocation.getLatitude())
                    .append(", Longitude: ").append(geographicalLocation.getLongitude())
                    .append(" at ").append(lastKnownLocation.getDate());
        }

        simpleMailMessage.setTo(recipient);
        simpleMailMessage.setSubject("Risk alert - " + user.getName());
        simpleMailMessage.setText(body.toString());

        return simpleMailMessage;
    }

    protected boolean isUserAtRisk(List<Context> contexts, Context lastContext) {
        if (lastContextIsBlackFlag(lastContext)) {
            return true;
        }

        if (worsenFromOrangeToRedFlag(contexts, lastContext)) {
            return true;
        }

        if (hasBlackFlag(contexts) && presentWorsen(contexts)) {
            return true;
        }

        int riskFlagsSum = contexts.stream().mapToInt(Context::getRiskFlag).sum();

        return hasNotPresentedYellowFlag(riskFlagsSum);
    }

    private boolean hasNotPresentedYellowFlag(int riskFlagsSum) {
        return riskFlagsSum > MAXIMUM_THRESHOLD;
    }

    private boolean worsenFromOrangeToRedFlag(List<Context> contexts, Context lastContext) {
        return lastContext.getRiskFlag() == RED_FLAG && contexts.get(contexts.size() - 2).getRiskFlag() == ORANGE_FLAG;
    }

    private boolean lastContextIsBlackFlag(Context lastContext) {
        return lastContext.getRiskFlag() == BLACK_FLAG;
    }

    private boolean presentWorsen(List<Context> contexts) {
        final Context lastContext = contexts.get(contexts.size() - 1);
        final Context penultimateContext = contexts.get(contexts.size() - 2);

        return lastContext.getRiskFlag() > penultimateContext.getRiskFlag();
    }

    private boolean hasBlackFlag(List<Context> contexts) {
        return contexts.stream().anyMatch(context -> context.getRiskFlag() == BLACK_FLAG);
    }

}
