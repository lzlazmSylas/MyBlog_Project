package com.lzl.service;

import com.lzl.po.User;

public interface UserService {
    User checkUser(String username, String password);
}
