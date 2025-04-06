package net.uncc.app.auth.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import net.uncc.app.auth.controller.dto.UserRegistrationDto;
import net.uncc.app.auth.model.User;

public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    User save(UserRegistrationDto registration);
}
