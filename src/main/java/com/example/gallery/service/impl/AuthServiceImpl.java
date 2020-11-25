package com.example.gallery.service.impl;

import com.example.gallery.model.Role;
import com.example.gallery.model.User;
import com.example.gallery.model.exception.PasswordDoesntMatchException;
import com.example.gallery.repository.RoleRepository;
import com.example.gallery.repository.UserRepository;
import com.example.gallery.service.AuthService;
import com.example.gallery.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserService userService;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository,
                           UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    @Override
    public User getCurrentUser() {
//        User user =  this.userRepository.getOne("emt-user");
//        if (user == null) {
//            user = new User();
//            user.setUsername("emt-user");
//            return this.userRepository.save(user);
//        }

//        return this.userRepository.findById("emt-user")
//                .orElseGet(() -> {
//                    User user = new User();
//                    user.setUsername("emt-user");
//                    return this.userRepository.save(user);
//                });

        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    }

    @Override
    public String getCurrentUserId() {
        return this.getCurrentUser().getUsername();
    }

    @Override
    public User signUpUser(String username,
                           String password,
                           String repeatedPassword) {
        if (!password.equals(repeatedPassword)) {
            throw new PasswordDoesntMatchException();
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        Role userRole = this.roleRepository.findByName("ROLE_USER");
        user.setRoles(Collections.singletonList(userRole));
        return this.userService.registerUser(user);
    }


    @PostConstruct
    public void init() {
        if (!this.userRepository.existsById("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(this.passwordEncoder.encode("admin"));
            admin.setRoles(this.roleRepository.findAll());
            this.userRepository.save(admin);
        }
    }
}
