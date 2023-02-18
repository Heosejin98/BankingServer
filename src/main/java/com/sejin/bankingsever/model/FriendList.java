package com.sejin.bankingsever.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
@NoArgsConstructor
@IdClass(FriendListCompositeKey.class)
public class FriendList {

    @Id
    @NonNull
    private Long userNo;

    @Id
    @NonNull
    @Column(nullable = false, length = 15)
    private String friendId;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean myBoolean = false;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}

