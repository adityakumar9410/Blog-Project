package com.aditya.myblogproject.services;

import com.aditya.myblogproject.models.Tag;
import com.aditya.myblogproject.repositories.TagRepository;
import org.apache.catalina.LifecycleState;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }


    public Tag saveTags(String blogTag) {
        Tag tag = new Tag();
        tag.setTagName(blogTag);
        Tag tag1 = tagRepository.findByTagName(blogTag);
        if (tag1==null){
            return tagRepository.save(tag);
        }

       return tag1;
    }
     public List<Tag>getAllTags(){
        return tagRepository.findAll();
    }
}
