package com.aditya.myblogproject.repositories;

import com.aditya.myblogproject.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
