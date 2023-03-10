package com.sejin.bankingsever.controller;

import com.sejin.bankingsever.dto.UserDTO;
import com.sejin.bankingsever.model.User;
import com.sejin.bankingsever.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<User> createUser(
        @RequestBody UserDTO userDTO
    ) {
        try {
            String userEmail = userDTO.getUserEmail();
            String passWord = userDTO.getPassWord();
            User user = userService.createUser(userEmail, passWord);

            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/checkId/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.existsById(userId));
    }
}
