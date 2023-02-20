package com.sejin.bankingsever.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserDTO {
    @NonNull
    private String userEmail;
    @NonNull
    private String passWord;
    @NonNull
    private String accountNumber;
}
