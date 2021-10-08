package com.aditya.myblogproject.service;

import com.aditya.myblogproject.model.Tag;
import com.aditya.myblogproject.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public List<String> getAllUniqueTags() {
        List<Tag> tags = this.tagRepository.findAll();
        Set<String> uniqueTags = new HashSet<>();
        for (Tag tag : tags) {
            if (!(uniqueTags.contains(tag.getTagName()))) {
                uniqueTags.add(tag.getTagName());
            }
        }
        return new ArrayList<>(uniqueTags);
    }
}
