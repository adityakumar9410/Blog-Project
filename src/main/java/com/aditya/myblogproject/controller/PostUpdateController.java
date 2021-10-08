package com.aditya.myblogproject.controller;

import com.aditya.myblogproject.model.Post;
import com.aditya.myblogproject.model.Tag;
import com.aditya.myblogproject.service.PostService;
import com.aditya.myblogproject.utils.DateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
public class PostUpdateController {

    private final PostService postService;

    public PostUpdateController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/updatePost/{id}")
    public String showUpdateForm(@PathVariable(value = "id") Integer id, Model model) {
        Post post = this.postService.getById(id);

        model.addAttribute("post", post);
        return "update_blog";
    }

    @PostMapping(value = "/updatePost", params = {"save"})
    public String updatePost(@ModelAttribute("post") Post post, BindingResult bindingResult) {
        String dateString = DateUtil.getCurrentInstanceOfDate();
        Date date = DateUtil.getDateFromDateString(dateString);
        post.setUpdateDate(date);
        List<Tag> tags = post.getTags();
        for (Tag tag : tags) {

            tag.setUpdateDate(date);
        }
        post.setTags(tags);
        this.postService.savePost(post);
        return "redirect:/";
    }

    @PostMapping(value = "/updatePost", params = {"addTag"})
    public String updateTag(final Post post,BindingResult bindingResult, Model model) {
        post.getTags().add(new Tag());
        model.addAttribute("post", post);
        return "update_blog";
    }

    @PostMapping(value = "/updatePost", params = {"removeTag"})
    public String removeBlogTag(final Post post, BindingResult bindingResult, final HttpServletRequest req , Model model) {
        final Integer tagId = Integer.valueOf(req.getParameter("removeTag"));
        post.getTags().remove(tagId.intValue());
        model.addAttribute("post", post);
        return "update_blog";
    }
}