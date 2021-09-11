package com.tcc.webserver.service;

import com.tcc.webserver.models.Location;
import com.tcc.webserver.models.User;

public interface UserService {

    User create(User user);

    Location getUserLastKnownLocation(User user);

}
