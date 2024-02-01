package com.easy.service;

import com.easy.dto.UserLoginDTO;
import com.easy.entity.User;

public interface UserService {
    /**
     * WeChat Login
     *
     * @param userLoginDTO
     * @return
     */
    User login(UserLoginDTO userLoginDTO);
}

