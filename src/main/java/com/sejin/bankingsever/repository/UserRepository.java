package com.sejin.bankingsever.repository;

import com.sejin.bankingsever.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsById(String id);

    Optional<User> findById(String id);
}