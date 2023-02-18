package com.sejin.bankingsever.service;

import com.sejin.bankingsever.model.FriendList;
import com.sejin.bankingsever.model.User;
import com.sejin.bankingsever.repository.FriendListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendListService {

    @Autowired
    FriendListRepository friendListRepository;

    public FriendList createFriendList(User user, String friendId) {
        return friendListRepository.save(new FriendList(user.getUserNo(), friendId, user));
    }
}
