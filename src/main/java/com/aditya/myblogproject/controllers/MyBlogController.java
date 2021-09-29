package com.aditya.myblogproject.controllers;

import com.aditya.myblogproject.models.Post;
import com.aditya.myblogproject.models.PostTag;
import com.aditya.myblogproject.models.PostTagCompositeId;
import com.aditya.myblogproject.services.PostService;
import com.aditya.myblogproject.services.PostTagService;
import com.aditya.myblogproject.services.TagService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class MyBlogController {

    private final PostService postService;
    private final TagService tagService;
    private final PostTagService postTagService;


    public MyBlogController(PostService postService, TagService tagService, PostTagService postTagService) {
        this.postService = postService;
        this.tagService = tagService;
        this.postTagService = postTagService;
    }

    @GetMapping
    public String getAllPosts(Model model) {
        //model.addAttribute("posts", postService.getAllPost())
        String keyword = null;
        return getPaginatedAndSorted(1, keyword, "publishDate","asc", model);
    }



    @GetMapping("/post{postId}")
    public String getBlogDetail(@PathVariable("postId") int postId, Model model){
        model.addAttribute("post", postService.getById(postId));
        model.addAttribute("postTags", postTagService.getAllPostTags());
        model.addAttribute("tags", tagService.getAllTags());
        return "blog_detail";
    }

    @GetMapping("/newpost")
    public String createBlogForm(Model model) {
        model.addAttribute("newPost", new Post());
        return "blog_form";
    }

    @PostMapping("/newpost")
    public String savePost(@RequestParam(name = "tag") String tag, @ModelAttribute("newBlog") Post post) {
        Integer tagId = tagService.saveTags(tag).getTagId();
        Integer postId = postService.savePost(post).getPostId();
        PostTag postTag = new PostTag(new PostTagCompositeId(postId, tagId));
        postTagService.savePostTag(postTag);

        return "redirect:/";
    }


    @GetMapping("/page/{pageNo}")
    public String getPaginatedAndSorted(@PathVariable(value = "pageNo") int pageNo,
                                        @RequestParam("keyword")String keyword,
                                        @RequestParam(value = "sortField" , defaultValue = "publishDate")String sortField,
                                        @RequestParam (value = "sortDir", defaultValue = "asc") String sortDir , Model model) {
        int pageSize = 10;
        Page<Post> page = postService.getSortedAndPaginatedData(pageNo,pageSize, keyword,sortField, sortDir);
        List<Post> posts = page.getContent();

        model.addAttribute("currPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("posts", posts);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc")?"desc":"asc");
        model.addAttribute("keyword", keyword);

        model.addAttribute("postTags", postTagService.getAllPostTags());

        model.addAttribute("tags", tagService.getAllTags());

        return "home";
    }



}
