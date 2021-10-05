package com.aditya.myblogproject.services;

import com.aditya.myblogproject.models.Post;
import com.aditya.myblogproject.models.Tag;
import com.aditya.myblogproject.repositories.PostRepository;
import com.aditya.myblogproject.repositories.TagRepository;
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

    public List<Post> getAllPost() {
        return postRepository.findAll();
    }

    public Post savePost(Post post) {
        post.setExcerpt(post.getContent().length() > 100 ? post.getContent().substring(0, 100) : post.getContent());
        return postRepository.save(post);
    }

    public Post getById(Integer id) {

        return postRepository.findById(id).get();
    }

    public Page<Post> getPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.postRepository.findAll(pageable);
    }

    public Page<Post> getSortedAndPaginatedData(int pageNo, int pageSize, String keyword, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        if (keyword != null) {
            return this.postRepository.findAll(keyword, pageable);
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

    public List<Post> getFilteredAndPaginatedData(int pageNo, int pageSize, List<String> authors,
                                                  List<String>publishDates, List<String>tagsChecked) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Set<String> uniqueAuthors = new HashSet<>(authors);
        Set<Date>blogPublishDates = new HashSet<>();
        for(String date :publishDates){
            Date pubDate = DateUtil.getDateFromDateString(date);
            blogPublishDates.add(pubDate);
        }

        Set<String>uniqueTags = new HashSet<>(tagsChecked);
        List<Post>allPosts = this.postRepository.findAll();
        Set<Post>postsByTags = new HashSet<>();
        for(Post post:allPosts){
            for(Tag tag:post.getTags()){
                if (uniqueTags.contains(tag.getTagName())){
                    postsByTags.add(post);
                }
            }
        }
        Set<Post>postsByAuthors = null;
        Page<Post>authorPage= this.postRepository.findPostsByAuthors(uniqueAuthors,pageable);
        postsByAuthors= new HashSet<>(authorPage.getContent());

        Set<Post>postsByDate=null;
        Page<Post>datePage=this.postRepository.findPostsByDates(blogPublishDates, pageable);
        postsByDate = new HashSet<>(datePage.getContent());
        Set<Post>posts = getIntersectionPosts(postsByTags, postsByAuthors, postsByDate);

        return new  ArrayList<>(posts);
    }

    private Set<Post> getIntersectionPosts(Set<Post> postsByTags, Set<Post> postsByAuthors, Set<Post> postsByDate) {
        Set<Post> interQrels = new HashSet<>(postsByAuthors);


            interQrels.retainAll(postsByTags);     // intersection with one

     /*  if (postsByAuthors != null && postsByAuthors.size() != 0){
           interQrels.retainAll(postsByAuthors);   // intersection with  two, one
       }*/

           interQrels.retainAll(postsByDate);    // intersection three, two, one


       return interQrels;
    }

    public List<String>getUniquePublishDates(){
        Set<String>publishDates = new HashSet<>();
        List<Post> posts = this.postRepository.findAll();

        for (Post post : posts) {
            String dateString = DateUtil.getDateStringFromDate(post.getPublishDate());
            if (!(publishDates.contains(dateString))){
                publishDates.add(dateString);
            }
        }
        return new ArrayList<>(publishDates);

    }

    public List<String>getAllTags(){
        List<Tag>tagList = this.tagRepository.findAll();
        Set<String>tags= new HashSet<>();
        for(Tag tag:tagList){
            if(!(tags.contains(tag.getTagName()))){
                tags.add(tag.getTagName());
            }
        }
        return new ArrayList<>(tags);
    }
}
