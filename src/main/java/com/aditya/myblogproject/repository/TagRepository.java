package com.aditya.myblogproject.repository;

import com.aditya.myblogproject.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Integer> {
     Tag findByTagName(String name);

}
