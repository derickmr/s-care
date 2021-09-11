package com.tcc.webserver.service.impl;

import com.tcc.webserver.models.Context;
import com.tcc.webserver.models.Location;
import com.tcc.webserver.models.User;
import com.tcc.webserver.repository.UserRepository;
import com.tcc.webserver.service.ContextService;
import com.tcc.webserver.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultUserService implements UserService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private ContextService contextService;

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public Location getUserLastKnownLocation(User user) {
        List<Context> contexts = contextService.getOrderedUserContexts(user);

        Optional<Location> userLocation = contexts.stream().map(Context::getLocation).findFirst();

        return userLocation.orElse(this.getUnknownLocation());
    }

    protected Location getUnknownLocation() {

        final Location unknownLocation = new Location();

        unknownLocation.setSemanticLocation("Desconhecido");

        return unknownLocation;

    }
}
