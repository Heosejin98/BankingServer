package com.sejin.bankingsever;

import com.sejin.bankingsever.controller.FriendListController;
import com.sejin.bankingsever.dto.FriendListDTO;
import com.sejin.bankingsever.exception.UserNotFoundException;
import com.sejin.bankingsever.model.FriendList;
import com.sejin.bankingsever.model.User;
import com.sejin.bankingsever.service.FriendListService;
import com.sejin.bankingsever.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FriendListControllerTest {


    @Autowired
    TestRestTemplate restTemplate;

    @Mock
    private FriendListService friendListService;

    @Mock
    private UserService userService;

    final String USER_EMAIL = "test";
    final String FRIEND_EMAIL = "freind_test";
    final String PASS_WORD = "pw_test";

    @InjectMocks
    private FriendListController friendListController;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("친구목록 api - (성공)")
    public void getFriendListTest() {
        // given
        Long userId = 1L;
        boolean friendStatus = true;
        List<FriendList> friendList = new ArrayList<>();
        friendList.add(new FriendList(
            userId, new User(USER_EMAIL, PASS_WORD)));
        when(friendListService.getFriendListByFriendStatus(anyLong(), anyBoolean())).thenReturn(
            friendList);
        when(userService.getUserById(anyLong())).thenReturn(new User(FRIEND_EMAIL, PASS_WORD));

        // when
        ResponseEntity<List<FriendListDTO>> responseEntity = friendListController.getFriendList(
            userId, friendStatus);

        // then
        assert responseEntity.getStatusCodeValue() == HttpStatus.OK.value();
        assert responseEntity.getBody().size() == friendList.size();
        assert responseEntity.getBody().get(0).getFriendEmail().equals(FRIEND_EMAIL);
    }

    @Test
    @DisplayName("친구목록 api - (성공 : 친구없음)")
    public void getFriendListNoFriendTest() {
        // given
        Long userId = 1L;
        boolean friendStatus = true;
        List<FriendList> friendList = new ArrayList<>();
        when(friendListService.getFriendListByFriendStatus(anyLong(), anyBoolean())).thenReturn(
            friendList);

        // when
        ResponseEntity<List<FriendListDTO>> responseEntity = friendListController.getFriendList(
            userId, friendStatus);

        // then
        assert responseEntity.getStatusCodeValue() == HttpStatus.OK.value();
        assert responseEntity.getBody().size() == friendList.size();
    }

    @Test
    @DisplayName("친구목록 api - (실패)")
    public void getFriendListInternalErrorTest() {
        // given
        Long userId = 1L;
        boolean friendStatus = true;
        when(friendListService.getFriendListByFriendStatus(anyLong(), anyBoolean())).thenThrow(
            new RuntimeException());

        // when
        ResponseEntity<List<FriendListDTO>> responseEntity = friendListController.getFriendList(
            userId, friendStatus);

        // then
        assert responseEntity.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR;
    }


    @Test
    @DisplayName("친구 요청 보내기 API - (성공)")
    public void sendFriendRequestTest() throws UserNotFoundException {
        // given
        Long userId = 1L;
        User user = new User(USER_EMAIL, PASS_WORD);
        User friendUser = new User(FRIEND_EMAIL, PASS_WORD);
        Long friendUserId = 2L;
        FriendList friendList = new FriendList(friendUserId, user);
        when(userService.getUserById(userId)).thenReturn(user);
        when(userService.getUserByUserEmail(FRIEND_EMAIL)).thenReturn(friendUser);
        when(friendListService.requestFriendList(user, friendUserId)).thenReturn(friendList);

        // when
        ResponseEntity<String> response = friendListController.sendFriendRequest(userId,
            FRIEND_EMAIL);

        // then
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody().equals(FRIEND_EMAIL + "님께 친구 요청을 보냈습니다.");
    }

    @Test
    @DisplayName("친구 요청 보내기 API - (실패 : UserNotFoundException)")
    public void sendFriendRequestUserNotFoundTest() throws UserNotFoundException {
        // given
        Long userId = 1L;
        when(userService.getUserByUserEmail(USER_EMAIL)).thenThrow(
            new UserNotFoundException("User Not Found"));

        // when
        ResponseEntity<String> response = friendListController.sendFriendRequest(userId,
            FRIEND_EMAIL);

        // then
        assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert response.getBody().equals("User : " + FRIEND_EMAIL + " 찾을 수 없습니다");
    }

    @Test
    @DisplayName("친구요청 api - (실패: 다른 사람의 아이디 입력)")
    public void sendFriendRequestInvalidUserTest() {
        // given
        Long userId = 1L;
        User user = new User(userId, USER_EMAIL, PASS_WORD, null, new ArrayList<>());
        when(userService.getUserById(userId)).thenReturn(user);
        when(userService.getUserByUserEmail(USER_EMAIL)).thenReturn(user);
        when(friendListService.isSelfMatch(any(User.class), anyLong())).thenReturn(true);

        // when
        ResponseEntity<String> responseEntity = friendListController.sendFriendRequest(userId,
            USER_EMAIL);

        // then
        assert responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert responseEntity.getBody().equals("다른 사람의 아이디를 입력하세요");
    }

    @Test
    @DisplayName("친구요청 api - (실패: 이미 친구인 경우)")
    public void sendFriendRequestAlreadyFriendTest() {
        // given
        Long userId = 1L;
        String friendEmail = "friend@test.com";
        when(userService.getUserById(userId)).thenReturn(new User(USER_EMAIL, PASS_WORD));
        when(userService.getUserByUserEmail(friendEmail)).thenReturn(
            new User(2L, FRIEND_EMAIL, PASS_WORD, null, new ArrayList<>())
        );
        when(friendListService.isSelfMatch(any(User.class), anyLong())).thenReturn(false);
        when(friendListService.hasFriendWithId(any(User.class), anyLong())).thenReturn(true);

        // when
        ResponseEntity<String> responseEntity = friendListController.sendFriendRequest(userId,
            friendEmail);

        // then
        assert responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert responseEntity.getBody().equals("이미 존재하는 친구입니다.");
    }
}