package com.aditya.myblogproject.repository;

import com.aditya.myblogproject.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;


public interface PostRepository extends JpaRepository<Post, Integer> {
    @Transactional
    @Query("SELECT DISTINCT p FROM Post p  JOIN p.tags t WHERE p.author LIKE  %?1%  OR p.title LIKE %?1% OR t.tagName LIKE %?1%")
    Page<Post>findAllPost(@Param("keyword") String keyword, Pageable pageable);

    @Transactional
    @Query("SELECT p FROM Post p WHERE  p.postId = :postId")
    Post  findPostById(@Param("postId")int postId);

    @Transactional
    @Query("SELECT p FROM Post p JOIN p.tags t WHERE p.author IN (?1) AND  p.publishDate IN(?2)  AND t.tagName IN(?3)")
    Page<Post>findAllPosts(@Param("authors")List<String>authors, @Param("dates")List<Date> publishDates , @Param("tagsChecked")List<String>tagsChecked,
                           Pageable pageable);

    @Transactional
    @Query("SELECT p FROM Post p  WHERE p.author IN (?1) AND  p.publishDate IN(?2) ")
    Page<Post>findAllPostByAuthorAndDate(@Param("authors")List<String>authors, @Param("dates")List<Date> publishDates , Pageable pageable);


    @Transactional
    @Query("SELECT p FROM Post p  JOIN p.tags t WHERE p.author IN (?1) AND  t.tagName IN(?2) ")
    Page<Post> findAllPostByAuthorAndTag(@Param("authors")List<String>authors,  @Param("tagsChecked")List<String>tagsChecked,Pageable pageable);

    @Transactional
    @Query("SELECT p FROM Post p  JOIN p.tags t WHERE p.publishDate IN (?1) AND  t.tagName IN(?2) ")
    Page<Post> findAllPostByDateAndTag(@Param("dates")List<Date> publishDates , @Param("tagsChecked")List<String>tagsChecked, Pageable pageable);


    @Transactional
    @Query("SELECT p FROM Post p  WHERE p.author IN(?1) ")
    Page<Post> findPostByAuthor(@Param("authors")List<String>authors, Pageable pageable);


    @Transactional
    @Query("SELECT p FROM Post p  WHERE  p.publishDate IN(?1) ")
    Page<Post> findPostByDate(@Param("dates")List<Date> publishDates , Pageable pageable);


    @Transactional
    @Query("SELECT p FROM Post p  JOIN p.tags t WHERE   t.tagName IN(?1) ")
    Page<Post> findPostByTag(@Param("tagsChecked")List<String>tagsChecked, Pageable pageable);

    @Transactional
    @Query("SELECT DISTINCT p FROM Post p  JOIN p.tags t WHERE p.author LIKE  %?1%  OR p.title LIKE %?1% OR t.tagName LIKE %?1%")
    List<Post>findPosts(@Param("keyword") String keyword);


}