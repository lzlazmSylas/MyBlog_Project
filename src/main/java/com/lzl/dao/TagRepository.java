package com.lzl.dao;

import com.lzl.po.Tag;

import com.lzl.po.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Long> {
    Tag findByName(String name);
    @Query("select g from t_tag g")
    List<Tag> findTop(Pageable pageable);
}
