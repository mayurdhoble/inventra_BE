package com.business.inventra.repository;

import com.business.inventra.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUserName(String userName);
    Optional<User> findByEmailOrUserName(String email, String userName);
    List<User> findByStatus(String status);
    boolean existsByEmail(String email);
    boolean existsByUserName(String userName);
} 