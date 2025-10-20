package com.practice.dvcs.service;

import com.practice.dvcs.dto.UserRequest;
import com.practice.dvcs.model.Role;
import com.practice.dvcs.model.User;
import com.practice.dvcs.repository.UserRepository;
import com.practice.dvcs.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public UserService(UserRepository userRepo, AuthenticationManager authManager, JwtService jwtService) {
        this.userRepo = userRepo;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }


    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public User createUser(UserRequest userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setRole(Role.USER);
        return userRepo.save(user);
    }

    public User getUserById(Long id) {
        return userRepo.findById(id).get();
    }

    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }


    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    public String verify(UserRequest user) {
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(user.getUsername());
        }
        return "invalid credentials";
    }


}

