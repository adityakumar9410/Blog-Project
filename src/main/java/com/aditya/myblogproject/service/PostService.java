package com.aditya.myblogproject.service;

import com.aditya.myblogproject.exception.ResourceNotFoundException;
import com.aditya.myblogproject.model.Post;
import com.aditya.myblogproject.model.Tag;
import com.aditya.myblogproject.repository.PostRepository;
import com.aditya.myblogproject.repository.TagRepository;
import com.aditya.myblogproject.utils.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    public PostService(PostRepository postRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post savePost(Post post) {
        post.setExcerpt(post.getContent().length() > 100 ? post.getContent().substring(0, 100) : post.getContent());
        return postRepository.save(post);
    }


    public Post getById(Integer id) {

        return this.postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
    }

    public Post updatePost(Post post, int id) {
        Post currentPost = this.postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        currentPost.setTitle(post.getTitle());
        currentPost.setContent(post.getContent());
        currentPost.setAuthor(post.getAuthor());
        postRepository.save(currentPost);
        return currentPost;
    }

    public void deletePostById(int postId) {
        postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));
        postRepository.deleteById(postId);
    }

    public void deletePost(int id) {
        this.postRepository.deleteById(id);
    }



    public Page<Post> getAllPaginatedPostData(int pageNo, int pageSize, String sortField, String sortDir, String keyword) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        if ( keyword!="") {

            return this.postRepository.findAllPost(keyword, pageable);

        }

        return this.postRepository.findAll(pageable);
    }

    public List<String> getAllUniqueAuthors() {
        Set<String> authors = new HashSet<>();
        List<Post> posts = this.postRepository.findAll();

        for (Post post : posts) {
            if (!(authors.contains(post.getAuthor()))) {
                authors.add(post.getAuthor());
            }
        }
        return new ArrayList<>(authors);
    }

    public Page<Post> getFilteredAndPaginatedData(int pageNo, int pageSize, List<String> authors,
                                                  List<String> publishDates, List<String> tagsChecked) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        List<Date> blogPublishDates = new ArrayList<>();
        for (String date : publishDates) {
            Date pubDate = DateUtil.getDateFromDateString(date);
            blogPublishDates.add(pubDate);
        }

        if (authors.size() != 0) {
            if (blogPublishDates.size() != 0) {
                if (tagsChecked.size() != 0) {
                    return this.postRepository.findAllPosts(authors, blogPublishDates, tagsChecked, pageable);
                } else {
                    return this.postRepository.findAllPostByAuthorAndDate(authors, blogPublishDates, pageable);
                }
            } else {
                if (tagsChecked.size() != 0) {
                    return this.postRepository.findAllPostByAuthorAndTag(authors, tagsChecked, pageable);
                } else {
                    return this.postRepository.findPostByAuthor(authors, pageable);
                }
            }
        } else {
            if (blogPublishDates.size() != 0) {
                if (tagsChecked.size() != 0) {
                    return this.postRepository.findAllPostByDateAndTag(blogPublishDates, tagsChecked, pageable);
                } else {
                    return this.postRepository.findPostByDate(blogPublishDates, pageable);
                }
            } else {
                if (tagsChecked.size() != 0) {
                    return this.postRepository.findPostByTag(tagsChecked, pageable);
                } else {
                    return this.postRepository.findAll(pageable);
                }
            }

        }
    }

    public List<String> getUniquePublishDates() {
        Set<String> publishDates = new HashSet<>();
        List<Post> posts = this.postRepository.findAll();

        for (Post post : posts) {
            String dateString = DateUtil.getDateStringFromDate(post.getPublishDate());
            if (!(publishDates.contains(dateString))) {
                publishDates.add(dateString);
            }
        }
        return new ArrayList<>(publishDates);
    }
}