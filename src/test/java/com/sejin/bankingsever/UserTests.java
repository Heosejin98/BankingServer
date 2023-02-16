package com.sejin.bankingsever;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.sejin.bankingsever.model.User;
import com.sejin.bankingsever.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("정상 회원 가입 케이스 테스트")
    @Transactional
    public void createUserTest() {

        // given
        User insertUser = userRepository.save((new User("test", "test")));

        // when
        User saveUser = userRepository.findById("test").orElse(null);

        // then
        assertThat(insertUser).isEqualTo(saveUser);
    }


    @Test
    @DisplayName("정상 회원 가입 api 테스트")
    @Transactional
    public void createUserApiTest() throws Exception {
        // given
        User user = new User("test", "pw_test");

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/user/signup")
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
            // then
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("test"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.passWord").value("pw_test"));
    }

    @Test
    @Rollback
    @DisplayName("중복 아이디 검증 테스트")
    public void existsByIdTest() {
        // given
        User user = new User("testId", "testPassword");
        userRepository.save(user);

        // when
        ResponseEntity<Boolean> response1 = restTemplate.getForEntity("/user/checkId/{id}",
            Boolean.class, "testId");
        ResponseEntity<Boolean> response2 = restTemplate.getForEntity("/user/checkId/{id}",
            Boolean.class, "절대없는아이디");

        // then
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response1.getBody()).isTrue();
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response2.getBody()).isFalse();
    }
}
