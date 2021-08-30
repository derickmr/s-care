package com.tcc.webserver.controller;

import com.tcc.webserver.models.User;
import com.tcc.webserver.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping
    public User createUser (@RequestBody User user) {

        return userService.create(user);

    }

}
