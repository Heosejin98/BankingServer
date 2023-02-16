package com.sejin.bankingsever.repository;

import com.sejin.bankingsever.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Long> {

}