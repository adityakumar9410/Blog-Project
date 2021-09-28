package com.aditya.myblogproject.repositories;

import com.aditya.myblogproject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
