package com.easy.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.easy.constant.MessageConstant;
import com.easy.dto.UserLoginDTO;
import com.easy.entity.User;
import com.easy.exception.BusinessException;
import com.easy.mapper.UserMapper;
import com.easy.properties.WeChatProperties;
import com.easy.service.UserService;
import com.easy.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    // WeChat service endpoint
    public static final String WEXIN_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(UserLoginDTO userLoginDTO) {
        //1. Call the WeChat API (HttpClient) to perform the login operation
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", weChatProperties.getAppid());
        paramMap.put("secret", weChatProperties.getSecret());
        paramMap.put("js_code", userLoginDTO.getCode());
        paramMap.put("grant_type", "authorization_code");

        String result = HttpClientUtil.doGet(WEXIN_LOGIN_URL, paramMap);
        log.info("WeChat login completed, result: {}", result);
        if (!StringUtils.hasLength(result)) {
            throw new BusinessException(MessageConstant.LOGIN_FAILED);
        }

        JSONObject jsonObject = JSON.parseObject(result);
        String openid = jsonObject.getString("openid"); // WeChat user's unique identifier
        if (!StringUtils.hasLength(openid)) {
            throw new BusinessException(MessageConstant.LOGIN_FAILED);
        }

        //2. If the user is accessing the mini program for the first time, complete the auto-registration (insert) functionality
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getOpenid, openid));
        if (user == null) {
            user = User.builder().openid(openid).createTime(LocalDateTime.now()).build();
            userMapper.insert(user);
        }

        //3. Return the user
        return user;
    }
}
