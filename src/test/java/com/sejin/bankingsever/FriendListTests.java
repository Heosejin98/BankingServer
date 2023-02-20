package com.sejin.bankingsever;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sejin.bankingsever.model.FriendList;
import com.sejin.bankingsever.model.User;
import com.sejin.bankingsever.service.FriendListService;
import com.sejin.bankingsever.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class FriendListTests {

    @Autowired
    FriendListService friendListService;

    @Autowired
    UserService userService;

    final String USER_EMAIL = "test";
    final String FRIEND_EMAIL = "freind_test";
    final String PASS_WORD = "pw_test";


    @Test
    @DisplayName("친구 요청 추가 테스트")
    public void requestFriendTest() {
        // given
        User user = userService.createUser(USER_EMAIL, PASS_WORD);
        User friend = userService.createUser(FRIEND_EMAIL, PASS_WORD);

        // when
        FriendList friendList = friendListService.requestFriendList(user, friend.getUserId());
        userService.addFriendList(user, friendList);
        userService.saveUser(user);
        boolean isExist1 = friendListService.hasFriendWithId(user, friend.getUserId());

        // then
        assertTrue(isExist1);
    }

    @Test
    @DisplayName("친구 요청 수락 테스트")
    public void acceptFriendTest() {
        // given
        User user = userService.createUser(USER_EMAIL, PASS_WORD);
        User friend = userService.createUser(FRIEND_EMAIL, PASS_WORD);
        FriendList friendList = friendListService.requestFriendList(user, friend.getUserId());
        userService.addFriendList(user, friendList);
        userService.saveUser(user);

        // when
        boolean isAccept1 = friendListService.acceptFriendRequest(user, friend.getUserId());

        // then
        assertTrue(isAccept1);
    }

}
