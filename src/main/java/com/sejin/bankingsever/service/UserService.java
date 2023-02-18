package com.sejin.bankingsever.service;

import com.sejin.bankingsever.model.User;
import com.sejin.bankingsever.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean existsById(String id) {
        return userRepository.existsById(id);
    }

    public User createUser(String id, String passWord) {
        return userRepository.save(new User(id, passWord));
    }
}
