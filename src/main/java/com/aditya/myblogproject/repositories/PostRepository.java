package com.aditya.myblogproject.repositories;

import com.aditya.myblogproject.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Transactional
    @Query("SELECT p FROM Post p WHERE "+
    "CONCAT(p.title, p.author, p.publishDate, p.content, p.excerpt)"+" LIKE  %?1%")
    Page<Post>findAll(String keyword, Pageable pageable);

    @Transactional
    @Query("SELECT p FROM Post p WHERE p.author IN :authors ")
    Page<Post>findPostsByAuthors(@Param("authors")Set<String>authors,  Pageable pageable);

    @Transactional
    @Query("SELECT p FROM Post p WHERE  p.publishDate IN :dates")
    Page<Post>findPostsByDates(@Param("dates")Set<Date>publishDates, Pageable pageable);

}
