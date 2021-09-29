package com.aditya.myblogproject.repositories;

import com.aditya.myblogproject.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    public Tag findByTagName(String name);
}