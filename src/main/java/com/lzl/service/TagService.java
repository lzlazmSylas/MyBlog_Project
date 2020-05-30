package com.lzl.service;

import com.lzl.po.Tag;
import com.lzl.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {
    Tag saveTag(Tag tag);
    Tag getTag(Long id);
    Tag getTagByName(String name);
    Tag updateTag(Long id, Tag tag);
    void deleteTag(Long id);
    Page<Tag> listTag(Pageable pageable);
    List<Tag> listTag();
    List<Tag> listTag(String ids);

    List<Tag> listTagTop(Integer size);
}
