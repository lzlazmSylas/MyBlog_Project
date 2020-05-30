package com.lzl.web.admin;


import com.lzl.po.Blog;
import com.lzl.po.User;
import com.lzl.service.BlogService;
import com.lzl.service.TagService;
import com.lzl.service.TypeService;
import com.lzl.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/admin")
public class blogController {

    private static final String INPUT = "admin/blogPost";
    private static final String LIST = "admin/blogManage";
    private static final String REDIRECT_LIST = "redirect:/admin/blogManage";
    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @GetMapping("/blogManage")
    public String blogPost(@PageableDefault(size= 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blog,
                           Model model){
        model.addAttribute("page", blogService.listBlog(pageable,blog)) ;
        model.addAttribute("type",typeService.listType());
    return "admin/blogManage";
    }

    @PostMapping("/blogManage/search")
    public String search(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog, Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blogManage :: blogList";
    }
    @GetMapping("/blogManage/input")
    public String input(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
        model.addAttribute("blog",new Blog());
    return INPUT;
    }
    @GetMapping("/blogManage/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
        Blog blog =blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        return  INPUT;
    }

    @GetMapping("/blogManage/post")
    public String postBlog(Blog blog, HttpSession httpSession, RedirectAttributes attributes){
        blog.setUser((User) httpSession.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog b;
        if (blog.getId()== null){
            b = blogService.saveBlog(blog);
        } else {
            b = blogService.updateBlog(blog.getId(), blog);
        }
        if (b == null ) {
            attributes.addFlashAttribute("message", "creation fails");
        } else {
            attributes.addFlashAttribute("message", "creation success");
        }

        return REDIRECT_LIST;
    }

    @GetMapping("/blogManage/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message","delete success");
        return REDIRECT_LIST;
    }
}
