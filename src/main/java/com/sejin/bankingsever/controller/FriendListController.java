package com.sejin.bankingsever.controller;

import com.sejin.bankingsever.dto.FriendRequestDTO;
import com.sejin.bankingsever.model.FriendList;
import com.sejin.bankingsever.model.User;
import com.sejin.bankingsever.service.FriendListService;
import com.sejin.bankingsever.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/friend")
public class FriendListController {

    @Autowired
    private UserService userService;

    @Autowired
    private FriendListService friendListService;

    @PostMapping("/{userId}")
    public ResponseEntity<String> sendFriendRequest(
        @PathVariable Long userId,
        @RequestBody FriendRequestDTO friendRequest
    ) {
        User user = userService.getUserById(userId);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        String friendId = friendRequest.getFriendId();
        if (!userService.existsById(friendId)) {
            return ResponseEntity.badRequest().body("존재하지 않는 아이디 입니다.");
        }
        if (user.getFriendLists().stream().anyMatch(f -> f.getFriendId().equals(friendId))) {
            return ResponseEntity.badRequest().body("이미 존재하는 친구입니다.");
        }
        FriendList friendList = friendListService.createFriendList(user, friendId);
        userService.addFriendList(user, friendList);
        userService.saveUser(user);
        return ResponseEntity.ok(friendId + "님께 친구 요청을 보냈습니다.");
    }
}
