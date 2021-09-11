package com.tcc.webserver.service;

import com.tcc.webserver.models.Context;
import com.tcc.webserver.models.User;

import java.util.List;

public interface ContextService {

    Context getLastContextFromUser(User user);

    List<Context> getLastSubContextsFromUser(User user, int range);

    List<Context> getOrderedUserContexts(User user);

}
