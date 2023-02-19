package com.sejin.bankingsever;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sejin.bankingsever.model.User;
import com.sejin.bankingsever.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class UserTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;


    final String USER_EMAIL = "test";
    final String PASS_WORD = "pw_test";

    @Test
    @DisplayName("정상 회원 가입 케이스 테스트")
    public void createUserTest() {
        // when
        userService.createUser(USER_EMAIL, PASS_WORD);
        boolean isExist = userService.existsById(USER_EMAIL);

        // then
        assertTrue(isExist);
    }


    @Test
    @DisplayName("정상 회원 가입 api 테스트")
    public void createUserApiTest() throws Exception {
        // given

        User user = new User(USER_EMAIL, PASS_WORD);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/user/signup")
                .content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
            // then
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.userEmail").value(USER_EMAIL))
            .andExpect(MockMvcResultMatchers.jsonPath("$.passWord").value(PASS_WORD));
    }
}
