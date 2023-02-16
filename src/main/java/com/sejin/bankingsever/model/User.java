package com.sejin.bankingsever.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Entity
@Getter
@RequiredArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo;

    @NonNull
    @Column(nullable = false, unique = true, length = 15)
    private String id;

    @NonNull
    @Column(nullable = false, length = 15)
    private String passWord;
}
