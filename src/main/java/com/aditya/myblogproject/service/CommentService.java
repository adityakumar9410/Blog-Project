package com.aditya.myblogproject.service;

import com.aditya.myblogproject.model.Comment;
import com.aditya.myblogproject.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment getCommentById(Integer cmtId) {
        return this.commentRepository.getById(cmtId);
    }

    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    public void deleteCommentById(Integer cmtId) {
        this.commentRepository.deleteById(cmtId);
    }
}