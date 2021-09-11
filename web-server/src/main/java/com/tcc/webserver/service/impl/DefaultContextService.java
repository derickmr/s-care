package com.tcc.webserver.service.impl;

import com.tcc.webserver.models.Context;
import com.tcc.webserver.models.User;
import com.tcc.webserver.repository.ContextRepository;
import com.tcc.webserver.service.ContextService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class DefaultContextService implements ContextService {

    @Resource
    private ContextRepository contextRepository;

    @Override
    public Context getLastContextFromUser(User user) {
        return this.getOrderedUserContexts(user).iterator().next();
    }

    @Override
    public List<Context> getLastSubContextsFromUser(User user, int range) {
        List<Context> contexts = this.getOrderedUserContexts(user);

        if (contexts.size() < range) {
            return contexts;
        }

        return contexts.subList(0, range);
    }

    @Override
    public List<Context> getOrderedUserContexts(User user) {
        final List<Context> contexts = user.getContexts();

        if (CollectionUtils.isEmpty(contexts)) {
            //TODO validate order (asc/desc)
            return contextRepository.getAllByUserOrderByDate(user);
        }

        contexts.sort(Comparator.comparing(Context::getDate));

        //TODO validate order (asc/desc)
        Collections.reverse(contexts);

        return contexts;
    }
}
