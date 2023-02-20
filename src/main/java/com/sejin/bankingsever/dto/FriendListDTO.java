package com.sejin.bankingsever.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FriendListDTO {

    @NonNull
    private String friendEmail;
}
