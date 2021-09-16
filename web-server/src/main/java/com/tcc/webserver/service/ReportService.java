package com.tcc.webserver.service;

import com.tcc.webserver.models.Context;
import com.tcc.webserver.models.User;

import java.util.Date;
import java.util.List;

public interface ReportService {

    List<Context> getContextsUntilDate(User user, Date date);

    List<Context> getContextsFromCurrentDay(User user);

    List<Context> getContextsWithinTimeFrame(User user, Date lowerDateLimit, Date higherDateLimit);

    Context getUserLastContext(User user);

}
