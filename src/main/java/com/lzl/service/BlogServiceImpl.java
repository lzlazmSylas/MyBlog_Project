package com.lzl.service;

import com.lzl.dao.BlogRepository;
import com.lzl.po.Blog;
import com.lzl.po.Type;
import com.lzl.util.MarkdownUtils;
import com.lzl.util.NullBeanUtils;
import com.lzl.vo.BlogQuery;
import javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.config.FreeMarkerConfigurerBeanDefinitionParser;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    BlogRepository blogRepository;
    @Override
    public Blog getBlog(Long id) {
        return blogRepository.findById(id).get();
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() !=null){
                    predicates.add(cb.like(root.<String>get("title"),"%"+ blog.getTitle()+"%"));
                }
                if(blog.getTypeId()!=null){
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if (blog.isRecommend()){
                    predicates.add(cb.equal(root.<Boolean>get("recommend"),blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public Page<Blog> ListBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                Join join = root.join("tags");
                return criteriaBuilder.equal(join.get("id"), tagId);
            }
        },pageable);
    }
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId()==null) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        }else blog.setUpdateTime(new Date());
        return blogRepository.save(blog);
    }
    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog blog1 = blogRepository.findById(id).get();
        if (blog1 == null){
            try {
                throw new NotFoundException("不存在该类型");
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }

        //过滤所有属性值为null的属性避免更新错误//
        BeanUtils.copyProperties(blog,blog1, NullBeanUtils.getNullPropertyNames(blog));
        blog1.setUpdateTime(new Date());
        return blogRepository.save(blog1);
    }

    @Override
    public Page<Blog> ListBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public List<Blog> ListRecommendBlog(Integer size) {
        Sort sort =  Sort.by(Sort.Direction.DESC,"updateTime");
       Pageable pageable1 = PageRequest.of(0, size, sort);
        return blogRepository.findTop(pageable1);
    }



    @Override
    public Page<Blog> searchResult(String query, Pageable pageable) {
        return blogRepository.findByQuery(query,pageable);
    }
    @Transactional
    @Override
    public Blog getConvert(Long id) {
        Blog b = blogRepository.findById(id).get();
        if (b == null) {
            try {
                throw new NotFoundException("tag does not exist");
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
            Blog blog2 = new Blog();
            BeanUtils.copyProperties(b,blog2);
            String content = blog2.getContent();;
            blog2.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
            blogRepository.updateViews(id);

        return blog2;
    }

    @Override
    public Map<String, List<Blog>> findYears() {
        List<String> years = blogRepository.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years){
            map.put(year,blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long cuntBlog() {
        return blogRepository.count();
    }

    @Override
    public void deleteBlog(Long id) {
    blogRepository.deleteById(id);
    }
}
