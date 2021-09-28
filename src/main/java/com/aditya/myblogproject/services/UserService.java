package com.aditya.myblogproject.services;

import com.aditya.myblogproject.models.User;
import com.aditya.myblogproject.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User  saveUser(User user){
        return userRepository.save(user);
    }
}
