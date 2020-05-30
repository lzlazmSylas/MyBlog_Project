package com.lzl.web;


import com.lzl.service.BlogService;
import com.lzl.service.TagService;
import com.lzl.service.TypeService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class indexController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @GetMapping("/")
    public String indexController(@PageableDefault(size=6,sort={"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {

    model.addAttribute("page", blogService.ListBlog(pageable));
    model.addAttribute("tags", tagService.listTagTop(10));
    model.addAttribute("types", typeService.listTypeTop(7));
    model.addAttribute("reconmmendBlogs", blogService.ListRecommendBlog(4));

        return "index";

    }
    @PostMapping("/search")
    public String blogSearch(@PageableDefault(size=6,sort={"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                             @RequestParam String query, Model model){

        model.addAttribute("page",blogService.searchResult("%"+ query+ "%", pageable));
        model.addAttribute("query",query);
        return"search";
    }
    @GetMapping("/blog/{id}")
    public String blogOne(@PathVariable Long id, Model model) throws NotFoundException {
        model.addAttribute("blog",blogService.getConvert(id));
        return "blog";
    }
    @GetMapping("/footer/newblog")
    public String blogList(Model model){

        model.addAttribute("newblogs", blogService.ListRecommendBlog(3));
        return "fragmentPlugin :: newblogList";
    }
}
