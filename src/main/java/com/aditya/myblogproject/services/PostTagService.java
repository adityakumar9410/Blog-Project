package com.aditya.myblogproject.services;

import com.aditya.myblogproject.models.PostTag;
import com.aditya.myblogproject.repositories.PostTagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostTagService {
    private final PostTagRepository postTagRepository;

    public PostTagService(PostTagRepository postTagRepository) {
        this.postTagRepository = postTagRepository;
    }

    public PostTag savePostTag(PostTag postTag){
        return postTagRepository.save(postTag);
    }

    public List<PostTag>getAllPostTags(){
        return postTagRepository.findAll();
    }
}
