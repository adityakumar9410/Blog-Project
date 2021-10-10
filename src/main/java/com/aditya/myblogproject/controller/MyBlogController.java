package com.aditya.myblogproject.controller;

import com.aditya.myblogproject.model.Comment;
import com.aditya.myblogproject.model.Post;

import com.aditya.myblogproject.model.Tag;
import com.aditya.myblogproject.model.User;
import com.aditya.myblogproject.service.CommentService;
import com.aditya.myblogproject.service.PostService;
import com.aditya.myblogproject.service.TagService;
import com.aditya.myblogproject.service.UserService;
import com.aditya.myblogproject.utils.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/")
public class MyBlogController {
    private final PostService postService;
    private final TagService tagService;
    private final CommentService commentService;
    private final UserService userService;



    public MyBlogController(PostService postService, TagService tagService, CommentService commentService, UserService userService) {
        this.postService = postService;
        this.tagService = tagService;
        this.commentService = commentService;

        this.userService = userService;
    }

    @GetMapping
    public String getHomePage(Model model) {
        return showByPage(1,"publishDate","asc", "",model);
    }

    @GetMapping("/page/{pageNo}")
    public String showByPage(@PathVariable(value = "pageNo" ) int pageNo, @RequestParam(value = "sortField" ,defaultValue = "publishDate")String sortField,
                            @RequestParam(value = "sortDir", defaultValue = "asc")String sortDir, @RequestParam(value = "keyword"  )String keyword, Model model) {
        int pageSize = 10;
        Page<Post> page = postService.getAllPaginatedPostData(pageNo, pageSize, sortField,sortDir, keyword);
        List<Post> posts = page.getContent();
        model.addAttribute("currPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("posts", posts);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);

        User user = new User();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserEmail = authentication.getName();
            user = userService.getUserByEmail(currentUserEmail);
        }

        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }


    @GetMapping("/post{postId}")
    public String getBlogDetail(@PathVariable("postId") int postId, Model model) {
        model.addAttribute("post", postService.getById(postId));
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
    public String addTag(final Post post, BindingResult bindingResult, Model model) {
        post.getTags().add(new Tag());
        model.addAttribute("newPost", post);
        return "blog_form";
    }

    @PostMapping(value = "/newpost", params = {"removeTag"})
    public String removeTag(final Post post, BindingResult bindingResult, final HttpServletRequest req, Model model) {
        final Integer tagId = Integer.valueOf(req.getParameter("removeTag"));
        post.getTags().remove(tagId.intValue());
        model.addAttribute("newPost", post);
        return "blog_form";
    }

    @PostMapping(value = "/newpost", params = {"save"})
    public String savePost(@ModelAttribute("newPost") Post post, BindingResult bindingResult) {
        String dateString = DateUtil.getCurrentInstanceOfDate();
        Date date = DateUtil.getDateFromDateString(dateString);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserEmail = authentication.getName();
            User   user = userService.getUserByEmail(currentUserEmail);

            post.setUser(user);
            post.setAuthor(user.getUserName());
        }

        post.setPublishDate(date);
        post.setPublished(true);
        List<Tag> tags = post.getTags();
        for (Tag tag : tags) {

            tag.setCreateDate(date);
        }
        post.setTags(tags);
        this.postService.savePost(post);
        return "redirect:/";
    }



    @GetMapping("/filter")
    public String filterPost(Model model) {

        return getFilteredAndPaginated(1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), model);
    }

    @GetMapping("/filter/pagination/{pageNo}")
    public String getFilteredAndPaginated(@PathVariable(value = "pageNo") int pageNo,
                                          @RequestParam(value = "authChecked", defaultValue = "") List<String> authChecked,
                                          @RequestParam(value = "dateChecked", defaultValue = "") List<String> dateChecked,
                                          @RequestParam(value = "tagsChecked" , defaultValue = "") List<String> tagsChecked, Model model) {
        int pageSize = 10;
        Page<Post> page = postService.getFilteredAndPaginatedData(pageNo, pageSize, authChecked, dateChecked, tagsChecked);
        List<Post>posts=page.getContent();
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        model.addAttribute("currPage", pageNo);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("posts", posts);

        List<String> authorList = postService.getAllUniqueAuthors();
        model.addAttribute("authors", authorList);

        List<String> postDates = postService.getUniquePublishDates();
        model.addAttribute("publishDates", postDates);

        List<String> tags = tagService.getAllUniqueTags();
        model.addAttribute("tags", tags);

        return "filter";
    }

    @GetMapping("/deletePost/{id}")
    public String deleteBlogPost(@PathVariable(value = "id") Integer id){
        this.postService.deletePost(id);
        return "redirect:/";
    }


    @GetMapping("/comment/{postId}")
    public String showComments(@PathVariable(value = "postId") Integer postId, Model model){
        Post post = postService.getById(postId);
        model.addAttribute("post", post);

        User user = new User();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserEmail = authentication.getName();
            user = userService.getUserByEmail(currentUserEmail);
        }

        model.addAttribute("user", user);

        return "post_comment";
    }

    @PostMapping("/comment/{postId}")
    public String saveComment(@PathVariable(value = "postId") Integer postId,@RequestParam("comment") String cmt, @RequestParam(value = "username", defaultValue ="" )String username){
        String dateString = DateUtil.getCurrentInstanceOfDate();
        Date date  = DateUtil.getDateFromDateString(dateString);
        Post post = postService.getById(postId);
        Comment comment = new Comment();

        if(username.length() != 0){
            comment.setUserName(username);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserEmail = authentication.getName();
            User   user = userService.getUserByEmail(currentUserEmail);
            comment.setUserName(user.getUserName());
        }

        comment.setComment(cmt);
        comment.setCreateDate(date);
        post.getComments().add(comment);
        this.postService.savePost(post);
        return "redirect:/";
    }

    @GetMapping("/editComment/{cmtId}")
    public String  getEditCommentForm(@PathVariable(value = "cmtId")Integer cmtId,  Model model){

        Comment comment =  commentService.getCommentById(cmtId);

        model.addAttribute("blogComment", comment);
        return "edit_comment";
    }

    @PostMapping("/editComment")
    public String editComment(@ModelAttribute("blogComment") Comment comment){
        String dateString = DateUtil.getCurrentInstanceOfDate();
        Date date = DateUtil.getDateFromDateString(dateString);
        comment.setUpdateDate(date);
        commentService.saveComment(comment);

        return "redirect:/";
    }

    @GetMapping("/deleteComment/{cmtId}")
    public String deleteComment(@PathVariable(value = "cmtId")Integer cmtId){
        this.commentService.deleteCommentById(cmtId);
        return "redirect:/";
    }

}