package com.example.gallery.service;

import com.example.gallery.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserService extends UserDetailsService {
    User findById(String username);
    User registerUser(User user);
}
