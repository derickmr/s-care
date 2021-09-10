package com.tcc.webserver.service.impl;

import com.tcc.webserver.models.Context;
import com.tcc.webserver.models.User;
import com.tcc.webserver.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class DefaultNotificationService implements NotificationService {

    private static final int BLACK_FLAG = 4;
    private static final int RED_FLAG = 3;
    private static final int ORANGE_FLAG = 2;
    private static final int YELLOW_FLAG = 1;
    private static final int MAXIMUM_THRESHOLD = 12;

    @Override
    public boolean shouldSendNotificationForUser(User user) {
        List<Context> contexts = getLastFiveContexts(user);
        final Context lastContext = contexts.get(contexts.size() - 1);

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

        if (hasNotPresentedYellowFlag(riskFlagsSum)) {
            return true;
        }

        return false;
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

    private List<Context> getLastFiveContexts(User user) {
        List<Context> contexts = user.getContexts();
        contexts.sort(Comparator.comparing(Context::getDate));
        return contexts.size() > 5 ? contexts.subList(0, 5) : contexts;
    }

    @Override
    public void sendEmailNotificationForUser(User user) {

    }
}
