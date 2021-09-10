package com.tcc.webserver.service;

import com.tcc.webserver.models.User;

public interface NotificationService {

    boolean shouldSendNotificationForUser(User user);

    void sendEmailNotificationForUser(User user);

}
