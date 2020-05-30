package com.lzl.web;

import com.lzl.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArchiveController {
    @Autowired
    private BlogService blogService;
    @GetMapping("/archives")
    public String findBlogsByYear(Model model){
        model.addAttribute("archive", blogService.findYears());
        model.addAttribute("totalNumBlogs",blogService.cuntBlog());
        return "archives";
    }
}
