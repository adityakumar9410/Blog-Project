package com.aditya.myblogproject.controllers;

import com.aditya.myblogproject.models.Post;

import com.aditya.myblogproject.models.Tag;
import com.aditya.myblogproject.services.PostService;
import com.aditya.myblogproject.services.TagService;
import com.aditya.myblogproject.utils.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    @GetMapping("/login")
    public String showLoginForm(){
        return "login";
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

        String dateString = DateUtil.getCurrentInstanceOfDate();
        Date date  = DateUtil.getDateFromDateString(dateString);
        post.setPublishDate(date);
        List<Tag> tags= post.getTags();
        for(Tag tag : tags){

            tag.setCreateDate(date);
        }
        post.setTags(tags);
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

        return "home";
    }

    @GetMapping("/filter")
    public  String filterPost( Model model){

        return  getFilteredAndPaginated(1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),model);
    }

    @GetMapping( "/filter/pagination/{pageNo}")
    public String  getFilteredAndPaginated(@PathVariable(value = "pageNo") int pageNo,
                                         @RequestParam("authChecked") List<String > authChecked,
                                         @RequestParam("dateChecked") List<String > dateChecked,
                                           @RequestParam("tagsChecked") List<String > tagsChecked,Model model){
        int pageSize = 10;
        if(authChecked ==null){
            authChecked = new ArrayList<>();
        }
        if(dateChecked==null){
            dateChecked=new ArrayList<>();
        }
        if(tagsChecked==null){
            tagsChecked=new ArrayList<>();
        }
        List<Post> posts = postService.getFilteredAndPaginatedData(pageNo,pageSize, authChecked, dateChecked, tagsChecked );
        int totalItems= posts.size();
        int totalPages =(int) Math.ceil((totalItems*1.0)/pageSize);
        model.addAttribute("currPage", pageNo);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("posts", posts);

        List<String> authorList = postService.getAllUniqueAuthors();
        model.addAttribute("authors", authorList);

        List<String>postDates = postService.getUniquePublishDates();
        model.addAttribute("publishDates",postDates);

        List<String>tags= tagService.getAllUniqueTags();
        model.addAttribute("tags", tags);

        return "filter";
    }

}
