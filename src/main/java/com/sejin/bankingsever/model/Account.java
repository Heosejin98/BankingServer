package com.sejin.bankingsever.model;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @NonNull
    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Setter
    @Getter
    @Column(columnDefinition = "DECIMAL(10,2) default '0.00'")
    private BigDecimal balance;

    @OneToOne
    @NonNull
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

}