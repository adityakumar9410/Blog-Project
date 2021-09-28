package com.aditya.myblogproject.repositories;

import com.aditya.myblogproject.models.PostTag;
import com.aditya.myblogproject.models.PostTagCompositeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<PostTag, PostTagCompositeId> {

}
