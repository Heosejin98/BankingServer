package com.sejin.bankingsever.model;

import com.sun.istack.NotNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@RequiredArgsConstructor
@NoArgsConstructor
public class FriendList {

    public static final boolean ACCEPT = true;
    public static final boolean REQUEST = false;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendId;

    @NonNull
    @Column(nullable = false, length = 15)
    private String friendEmail;

    @Setter
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean friendStatus = REQUEST;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}

