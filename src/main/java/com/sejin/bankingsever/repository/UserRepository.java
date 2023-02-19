package com.sejin.bankingsever.repository;

import com.sejin.bankingsever.model.FriendList;
import com.sejin.bankingsever.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUserEmail(String userEmail);
}