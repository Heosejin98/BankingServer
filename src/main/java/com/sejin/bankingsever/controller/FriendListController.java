package com.sejin.bankingsever.controller;

import com.sejin.bankingsever.dto.FriendRequestDTO;
import com.sejin.bankingsever.exception.UserNotFoundException;
import com.sejin.bankingsever.model.FriendList;
import com.sejin.bankingsever.model.User;
import com.sejin.bankingsever.service.FriendListService;
import com.sejin.bankingsever.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    void sendFriendRequestException(User user, String friendEmail, Long userId) {
        if (user == null) {
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }
        if (!userService.existsById(friendEmail)) {
            throw new IllegalArgumentException("존재하지 않는 아이디 입니다.");
        }
        if (friendListService.isSelfMatch(user, friendEmail)) {
            throw new IllegalArgumentException("다른 사람의 아이디를 입력하세요");
        }
        if (friendListService.hasFriendWithId(user, friendEmail)) {
            throw new IllegalArgumentException("이미 존재하는 친구입니다.");
        }
    }
    @PostMapping("/request/{userId}")
    public ResponseEntity<String> sendFriendRequest(
        @PathVariable Long userId,
        @RequestBody FriendRequestDTO friendRequest
    ) {

        try {
            User user = userService.getUserById(userId);
            String friendEmail = friendRequest.getFriendEmail();

            sendFriendRequestException(user, friendEmail, userId);

            FriendList friendList = friendListService.requestFriendList(user, friendEmail);
            userService.addFriendList(user, friendList);
            userService.saveUser(user);

            return ResponseEntity.ok(friendEmail + "님께 친구 요청을 보냈습니다.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/acceptance/{userId}")
    public ResponseEntity<String> updateFriendStatus(
        @PathVariable Long userId,
        @RequestBody FriendRequestDTO friendRequestDTO
    ) {
        User user = userService.getUserById(userId);
        boolean isAccept = friendListService.acceptFriendRequest(user, friendRequestDTO.getFriendEmail());

        if (isAccept) {
            return ResponseEntity.ok("친구 수락");
        }
        return ResponseEntity.badRequest().body("친구 추가 실패");
    }
}
