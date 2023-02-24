package com.sejin.bankingsever.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class CreateAccountResponseDTO {
    private String accountNumber;
    private String userEmail;
}
