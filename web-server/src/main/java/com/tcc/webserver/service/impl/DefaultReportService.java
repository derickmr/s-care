package com.tcc.webserver.service.impl;

import com.tcc.webserver.models.Context;
import com.tcc.webserver.models.User;
import com.tcc.webserver.service.ContextService;
import com.tcc.webserver.service.ReportService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultReportService implements ReportService {

    @Resource
    private ContextService contextService;

    @Override
    public List<Context> getContextsUntilDate(User user, Date limitDate) {
        List<Context> contexts = contextService.getOrderedUserContexts(user);

        return contexts.stream().filter(
                context -> context.getDate().after(limitDate)
        ).collect(Collectors.toList());
    }

    @Override
    public List<Context> getContextsFromCurrentDay(User user) {
        List<Context> contexts = contextService.getOrderedUserContexts(user);

        Date currentDate = new Date();

        return contexts.stream().filter(
                context -> DateUtils.isSameDay(context.getDate(), currentDate)
        ).collect(Collectors.toList());
    }

    @Override
    public List<Context> getContextsWithinTimeFrame(User user, Date lowerDateLimit, Date higherDateLimit) {
        List<Context> contexts = contextService.getOrderedUserContexts(user);

        return contexts.stream().filter(
                context -> context.getDate().after(lowerDateLimit) && context.getDate().before(higherDateLimit)
        ).collect(Collectors.toList());
    }

    @Override
    public Context getUserLastContext(User user) {
        return this.contextService.getLastContextFromUser(user);
    }
}
