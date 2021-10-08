package com.aditya.myblogproject.repository;

import com.aditya.myblogproject.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
