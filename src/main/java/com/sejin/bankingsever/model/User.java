package com.sejin.bankingsever.model;

import java.util.List;
import java.util.ArrayList;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@RequiredArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NonNull
    @Column(nullable = false, unique = true, length = 15)
    private String userEmail;

    @NonNull
    @Column(nullable = false, length = 15)
    private String passWord;

    @NonNull
    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Setter
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendList> friendLists = new ArrayList<>();
}
