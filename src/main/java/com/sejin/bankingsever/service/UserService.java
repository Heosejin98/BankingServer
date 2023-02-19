package com.sejin.bankingsever.service;

import com.sejin.bankingsever.model.FriendList;
import com.sejin.bankingsever.model.User;
import com.sejin.bankingsever.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean existsById(String userEmail) {
        return userRepository.existsByUserEmail(userEmail);
    }

    public User createUser(String userEmail, String passWord) {
        return userRepository.save(new User(userEmail, passWord));
    }


    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void addFriendList(User user, FriendList friendList) {
        List<FriendList> friendLists = user.getFriendLists();
        friendLists.add(friendList);
        user.setFriendLists(friendLists);
    }


    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
