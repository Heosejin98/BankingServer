package com.sejin.bankingsever.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO<T> {

        private boolean success;
        private T data;
        private ApiErrorDTO error;

}
