package com.aditya.myblogproject.api;

import com.aditya.myblogproject.model.JwtResponse;
import com.aditya.myblogproject.model.Post;
import com.aditya.myblogproject.model.Tag;
import com.aditya.myblogproject.model.User;
import com.aditya.myblogproject.service.PostService;
import com.aditya.myblogproject.service.UserService;
import com.aditya.myblogproject.utils.DateUtil;
import com.aditya.myblogproject.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PostRestController {
    @Autowired
    private final PostService postService;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtUtil jwtUtil;
    @Autowired
    private final AuthenticationManager authenticationManager;

    public PostRestController(PostService postService, UserService userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.postService = postService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody User user) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid email or password");
        }
        final UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable int postId) {
        return new ResponseEntity<Post>(postService.getById(postId), HttpStatus.OK);
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> savePost(@RequestBody Post post) {
        String dateString = DateUtil.getCurrentInstanceOfDate();
        Date date = DateUtil.getDateFromDateString(dateString);
        post.setPublishDate(date);
        List<Tag> tags = post.getTags();
        for (Tag tag : tags) {
            tag.setCreateDate(date);
        }
        post.setTags(tags);
        return new ResponseEntity<>(postService.savePost(post), HttpStatus.CREATED);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable("postId") int postId, @RequestBody Post post) {

        return new ResponseEntity<>(postService.updatePost(post, postId), HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<String> deletePostById(@PathVariable int postId) {
        postService.deletePostById(postId);
        return new ResponseEntity<>("Post deleted Successfully", HttpStatus.OK);
    }

    @GetMapping("/posts")
    public Page<Post> getPaginatedPosts(
            @RequestParam Optional<Integer> start,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<String> sortField,
            @RequestParam Optional<String>sortDir
    ) {
        return postService.getAllPaginatedPostWithSorting(start, limit, sortField, sortDir);
    }

    @GetMapping("/posts/searchPost")
    public List<Post> searchPost(
            @RequestParam Optional<String> search
    ) {
        return postService.searchPost(search);
    }

    @GetMapping("/posts/filter")
    public Page<Post> getFilteredPost(
            @RequestParam Optional<Integer> start,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<List<String>> authors,
            @RequestParam Optional<List<String>> publishDates,
            @RequestParam Optional<List<String>> tags
    ) {
        List<String> args = new ArrayList<>();
        return postService.getFilteredAndPaginatedData(start.orElse(1), limit.orElse(10), authors.orElse(args),
                publishDates.orElse(args), tags.orElse(args));
    }
}