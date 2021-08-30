package com.tcc.webserver.service.impl;

import com.tcc.webserver.models.User;
import com.tcc.webserver.repository.UserRepository;
import com.tcc.webserver.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DefaultUserService implements UserService {

    @Resource
    private UserRepository userRepository;

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }
}
