package com.lzl.service;

import com.lzl.dao.UserRepository;
import com.lzl.po.User;
import com.lzl.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

        @Override
        public User checkUser(String username, String password){
            User user = userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));

            return user;
        }

}