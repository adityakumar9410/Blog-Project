package com.aditya.myblogproject.services;

import com.aditya.myblogproject.models.Post;
import com.aditya.myblogproject.repositories.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPost(){
        return postRepository.findAll();
    }

    public Post savePost(Post post){
        post.setExcerpt(post.getContent().length()>100? post.getContent().substring(0,100): post.getContent());
        return postRepository.save(post);
    }

    public Post getById(Integer id){

        return postRepository.findById(id).get();
    }

    public Page<Post> getPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        return this.postRepository.findAll(pageable);
    }

    public Page<Post> getSortedAndPaginatedData(int pageNo, int pageSize, String keyword,String sortField, String sortDir){
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortField).ascending():Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo-1, pageSize, sort);
        if(keyword!=null){
            return this.postRepository.findAll(keyword, pageable);
        }
        return this.postRepository.findAll(pageable);
    }
}
