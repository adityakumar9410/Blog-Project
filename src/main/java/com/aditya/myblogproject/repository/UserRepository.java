package com.aditya.myblogproject.repository;

import com.aditya.myblogproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {
      @Transactional
      @Query("SELECT u FROM User u WHERE u.email = :email")
        User findByEmail(@Param("email") String email);
}
