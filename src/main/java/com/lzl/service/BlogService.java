package com.lzl.service;

import com.lzl.po.Blog;
import com.lzl.vo.BlogQuery;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

public interface BlogService {

    Blog getBlog(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);
    Page<Blog> ListBlog(Pageable pageable);
    Page<Blog> ListBlog(Long tagId, Pageable pageable);

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id,Blog blog);
    List<Blog> ListRecommendBlog(Integer size);
    Page<Blog> searchResult(String query, Pageable pageable);
    Blog getConvert(Long id) throws NotFoundException;

    Map<String, List<Blog>> findYears();
    Long cuntBlog();

    void deleteBlog(Long id);
}
