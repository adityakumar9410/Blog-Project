package com.aditya.myblogproject.controllers;

import com.aditya.myblogproject.models.Post;

import com.aditya.myblogproject.models.Tag;
import com.aditya.myblogproject.services.PostService;
import com.aditya.myblogproject.services.TagService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class MyBlogController {
    private final PostService postService;
    private final TagService tagService;



    public MyBlogController(PostService postService, TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }

    @GetMapping
    public String getAllPosts(Model model) {
        String keyword = null;
        return getPaginatedAndSorted(1, keyword, "publishDate","asc", model);
    }



    @GetMapping("/post{postId}")
    public String getBlogDetail(@PathVariable("postId") int postId, Model model){
        model.addAttribute("post", postService.getById(postId));
        //model.addAttribute("postTags", postTagService.getAllPostTags());
        model.addAttribute("tags", tagService.getAllTags());
        return "blog_detail";
    }

    @GetMapping("/newpost")
    public String createBlogForm(Model model) {
        Post post = new Post();
        model.addAttribute("newPost", post);
        return "blog_form";
    }

    @PostMapping(value = "/newpost", params = {"addTag"})
    public String addTag( final Post post , BindingResult bindingResult, Model model) {
            post.getTags().add(new Tag());
            model.addAttribute("newPost", post);
        return "blog_form";
    }

    @PostMapping(value="/newpost", params={"removeTag"})
    public String removeRow(final Post post, BindingResult bindingResult, final HttpServletRequest req, Model model) {
        final Integer tagId = Integer.valueOf(req.getParameter("removeTag"));
        post.getTags().remove(tagId.intValue());
        model.addAttribute("newPost", post);
        return "blog_form";
    }

    @PostMapping(value = "/newpost" , params = {"save"})
    public String savePost( @ModelAttribute("newPost") Post post, BindingResult bindingResult) {

        this.postService.savePost(post);
        return "redirect:/";
    }


    @GetMapping("/page/{pageNo}")
    public String getPaginatedAndSorted(@PathVariable(value = "pageNo") int pageNo,
                                        @RequestParam("keyword")String keyword,
                                        @RequestParam(value = "sortField" , defaultValue = "publishDate")String sortField,
                                        @RequestParam (value = "sortDir", defaultValue = "asc") String sortDir , Model model) {
        int pageSize = 10;
        Page<Post>   page = postService.getSortedAndPaginatedData(pageNo, pageSize, keyword, sortField, sortDir);
        List<Post> posts = page.getContent();

        model.addAttribute("currPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("posts", posts);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc")?"desc":"asc");
        model.addAttribute("keyword", keyword);

       // model.addAttribute("tags", tagService.getAllTags());

        return "home";
    }

      @GetMapping("/filter")
    public  String filterPost( Model model){
        List<String >authors=new ArrayList<>();
        return  getFilteredAndPaginated(1, authors, model);
    }

    @GetMapping( "/filter/pagination/{pageNo}")
    public String  getFilteredAndPaginated(@PathVariable(value = "pageNo") int pageNo,
                                         @RequestParam("authChecked") List<String > authChecked,Model model){
        int pageSize = 10;
        Page<Post> page = postService.getFilteredAndPaginatedData(pageNo,pageSize, authChecked );
        int totalPages = page.getTotalPages();
        int totalItems=(int)page.getTotalElements();
        List<Post> posts = page.getContent();
        //System.out.println("Total items are :"+totalItems);
        model.addAttribute("currPage", pageNo);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("posts", posts);

        List<String> authorList = postService.getAllUniqueAuthors();
        //System.out.println(authorList.get(0));
        model.addAttribute("authors", authorList);
        //model.addAttribute("postTags", postTagService.getAllPostTags());
        //model.addAttribute("tags", tagService.getAllTags());

        return "filter";
    }

}
