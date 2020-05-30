package com.lzl.service;

import com.lzl.dao.TagRepository;
import com.lzl.po.Tag;
import javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService{

    @Autowired
    private TagRepository tagRepository;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
    return tagRepository.save(tag);
    }
    @Transactional
    @Override
    public Tag getTag(Long id) {
        return tagRepository.findById(id).get();
    }
    @Transactional
    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag tag1 = tagRepository.findById(id).get();
        if (tag1 == null) {
            try {
                throw new NotFoundException("tag does not exist");
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
        BeanUtils.copyProperties(tag, tag1);
        return tagRepository.save(tag1);
    }
    @Transactional
    @Override
    public void deleteTag(Long id) {
    tagRepository.deleteById(id);
    }
    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }
    //One blog have many tags as form of string,that;s why we need convert to list<tag>. list object is used to show tag name//
    @Override
    public List<Tag> listTag(String ids) {
        return tagRepository.findAllById(convertList(ids));
    }
    //findallbyid 不接受string类型所以要转换//
    public  List<Long> convertList(String ids){
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids!=null)   {
            String[] idarray = ids.split(",");
            for (int i=0; i<idarray.length; i++){
                list.add(Long.valueOf(idarray[i]));
            }

        }
        return list;
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable =  PageRequest.of(0, size, sort);
        return tagRepository.findTop(pageable);
    }
}
