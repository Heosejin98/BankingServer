package com.sejin.bankingsever.controller;


import static com.sejin.bankingsever.util.AccountUtil.generateAccountNumber;

import com.sejin.bankingsever.dto.ApiErrorDTO;
import com.sejin.bankingsever.dto.CreateAccountDTO;
import com.sejin.bankingsever.dto.CreateAccountResponseDTO;
import com.sejin.bankingsever.dto.ResponseDTO;
import com.sejin.bankingsever.model.User;
import com.sejin.bankingsever.service.AccountService;
import com.sejin.bankingsever.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/account")
public class   AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    UserService userService;

    final String CREATE_FAIL_MESSAGE = "이미 계좌가 있습니다";

    public ResponseDTO<CreateAccountResponseDTO> handleCreateRequest(
        CreateAccountDTO createAccountDTO
    ) {
        Long userId = createAccountDTO.getUserId();
        User user = userService.getUserById(userId);
        String userEmail = user.getUserEmail();
        String accountNumber = generateAccountNumber(userId);

        if (accountService.existsByUserId(userId)) {
            ApiErrorDTO error = new ApiErrorDTO(HttpStatus.BAD_REQUEST, CREATE_FAIL_MESSAGE, null);
            return new ResponseDTO<>(false, null, error);
        }
        accountService.createAccount(userId, accountNumber);
        CreateAccountResponseDTO createAccountResponseDTO = new CreateAccountResponseDTO(accountNumber, userEmail);
        return new ResponseDTO<>(true, createAccountResponseDTO, null);
    }
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO<CreateAccountResponseDTO>> createAccount(
        @RequestBody CreateAccountDTO createAccountDTO
    ) {
        ResponseDTO<CreateAccountResponseDTO> responseDTO = handleCreateRequest(createAccountDTO);

        return ResponseEntity.ok(responseDTO);
    }
}
