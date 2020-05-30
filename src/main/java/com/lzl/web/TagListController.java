package com.lzl.web;

import com.lzl.po.Tag;
import com.lzl.service.BlogService;
import com.lzl.service.TagService;
import com.lzl.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TagListController {
    @Autowired
    BlogService blogService;
    @Autowired
    TagService tagService;
    @GetMapping("/tags/{id}")
    public String listTag(@PageableDefault(size=6,sort={"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable
            , @PathVariable Long id
            ,Model model){
        List<Tag> tags = tagService.listTagTop(1000);
        if (id == -1){
            id= tags.get(0).getId();
        }
        model.addAttribute("tags",tags);
        model.addAttribute("page",blogService.ListBlog(id,pageable));
        model.addAttribute("activeTag", id);

        return "tags";
    }
}
