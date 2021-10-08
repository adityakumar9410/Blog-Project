package com.aditya.myblogproject.api;

import com.aditya.myblogproject.model.Post;
import com.aditya.myblogproject.model.Tag;
import com.aditya.myblogproject.service.PostService;
import com.aditya.myblogproject.utils.DateUtil;
import org.springframework.core.metrics.StartupStep;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {
    private final PostService postService;

    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping()
    public List<Post> getAllPosts(){
            return this.postService.getAllPost();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post>getPostById(@PathVariable("postId") int postId){
        return new ResponseEntity<Post>(postService.getById(postId),HttpStatus.OK );
    }

    @PostMapping()
   public ResponseEntity<Post> savePost(@RequestBody Post post){
        String dateString = DateUtil.getCurrentInstanceOfDate();
        Date date = DateUtil.getDateFromDateString(dateString);
        post.setPublishDate(date);
         List<Tag> tags= post.getTags();
         for(Tag tag: tags){
             tag.setCreateDate(date);
         }
         post.setTags(tags);
        return new ResponseEntity<Post>(postService.savePost(post), HttpStatus.CREATED);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Post>updatePost(@PathVariable("postId")int postId, @RequestBody Post post){
            return new ResponseEntity<Post>(postService.updatePost(post, postId), HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public  ResponseEntity<String>deletePostById(@PathVariable("postId")int postId){
        postService.deletePostById(postId);
        return new  ResponseEntity<String>("Post deleted Successfully", HttpStatus.OK);
    }
}
