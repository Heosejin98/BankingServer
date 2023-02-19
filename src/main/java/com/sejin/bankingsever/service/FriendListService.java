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

    public FriendList requestFriendList(User user, String friendId) {
        return friendListRepository.save(new FriendList(friendId, user));
    }

    public boolean hasFriendWithId(User user, String friendEmail) {
        return user.getFriendLists().stream().anyMatch(f -> f.getFriendEmail().equals(friendEmail));
    }

    public boolean isSelfMatch(User user, String friendEmail){
        return  user.getUserEmail().equals(friendEmail);
    }
    public boolean acceptFriendRequest(User user, String friendEmail) {
        FriendList friendList = friendListRepository
                                .findByFriendEmailAndUser(friendEmail, user)
                                .orElse(null);
        if (friendList == null) {
            return false;
        }
        friendList.setFriendStatus(FriendList.ACCEPT);
        friendListRepository.save(friendList);
        return true;
    }
}
