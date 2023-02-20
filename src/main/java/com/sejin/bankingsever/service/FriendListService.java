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

    public FriendList requestFriendList(User user, Long friendUserId) {
        return friendListRepository.save(new FriendList(friendUserId, user));
    }

    public boolean hasFriendWithId(User user, Long friendUserId) {
        return user.getFriendLists().stream().anyMatch(f -> f.getFriendUserId().equals(friendUserId));
    }

    public boolean isSelfMatch(User user, Long friendUserId){
        return  user.getUserId().equals(friendUserId);
    }
    public boolean acceptFriendRequest(User user, Long friendUserId) {
        FriendList friendList = friendListRepository
                                .findByFriendUserIdAndUser(friendUserId, user)
                                .orElse(null);
        if (friendList == null) {
            return false;
        }
        friendList.setFriendStatus(FriendList.ACCEPT);
        friendListRepository.save(friendList);
        return true;
    }
}
