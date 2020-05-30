package com.lzl.service;

import com.lzl.po.Comment;

import java.util.List;

public interface CommentService {

    public List<Comment> listCommentByBlogId(Long BlogId);

    public Comment saveComment(Comment comment);
}
