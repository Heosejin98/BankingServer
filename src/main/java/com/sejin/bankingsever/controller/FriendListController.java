package com.sejin.bankingsever.controller;

import com.sejin.bankingsever.dto.FriendListDTO;
import com.sejin.bankingsever.exception.UserNotFoundException;
import com.sejin.bankingsever.model.FriendList;
import com.sejin.bankingsever.model.User;
import com.sejin.bankingsever.service.FriendListService;
import com.sejin.bankingsever.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/friend")
public class FriendListController {

    @Autowired
    private UserService userService;

    @Autowired
    private FriendListService friendListService;

    private Long getFriendUserId(
        User friendUser,
        String friendEmail
    ) {
        if (friendUser == null) {
            throw new UserNotFoundException("User : " + friendEmail + " 찾을 수 없습니다");
        }
        return friendUser.getUserId();
    }
    @PostMapping("/request")
    public ResponseEntity<String> sendFriendRequest(
        @RequestParam Long userId,
        @RequestParam String friendEmail
    ) {

        try {
            User user = userService.getUserById(userId);
            User friendUser = userService.getUserByUserEmail(friendEmail);
            Long friendUserId = getFriendUserId(friendUser, friendEmail);

            if (friendListService.isSelfMatch(user, friendUserId)) {
                throw new IllegalArgumentException("다른 사람의 아이디를 입력하세요");
            }
            if (friendListService.hasFriendWithId(user, friendUserId)) {
                throw new IllegalArgumentException("이미 존재하는 친구입니다.");
            }

            FriendList friendList = friendListService.requestFriendList(user, friendUserId);
            userService.addFriendList(user, friendList);
            userService.saveUser(user);

            return ResponseEntity.ok(friendUser.getUserEmail() + "님께 친구 요청을 보냈습니다.");
        } catch (UserNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/acceptance/{userId}")
    public ResponseEntity<String> updateFriendStatus(
        @PathVariable Long userId,
        @RequestBody FriendListDTO friendListDTO
    ) {
        try {
            User user = userService.getUserById(userId);
            String friendEmail = friendListDTO.getFriendEmail();
            User friendUser = userService.getUserByUserEmail(friendEmail);
            Long friendUserId = getFriendUserId(friendUser,friendEmail);

            boolean isAccept = friendListService.acceptFriendRequest(user, friendUserId);

            if (isAccept) {
                return ResponseEntity.ok("친구 수락");
            }
            return ResponseEntity.ok("친구 추가 실패");
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<FriendListDTO>> getFriendList(
        @RequestParam Long userId,
        @RequestParam boolean friendStatus
    ) {
        try {
            List<FriendList> friendList = friendListService.getFriendListByFriendStatus(userId, friendStatus);
            List<FriendListDTO> friendListDTOs = new ArrayList<>();

            for (FriendList friend : friendList) {
                User friendUser = userService.getUserById(friend.getFriendUserId());
                String friendUserEmail = friendUser.getUserEmail();
                FriendListDTO friendRequestDTO = new FriendListDTO(friendUserEmail);
                friendListDTOs.add(friendRequestDTO);
            }

            return ResponseEntity.ok(friendListDTOs);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
