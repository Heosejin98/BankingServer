package com.sejin.bankingsever.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorDTO {
    private HttpStatus code;
    private String message;
    private List<String> details;
}
