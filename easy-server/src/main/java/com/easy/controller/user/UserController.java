package com.easy.controller.user;

import com.easy.constant.JwtClaimsConstant;
import com.easy.dto.UserLoginDTO;
import com.easy.entity.User;
import com.easy.properties.JwtProperties;
import com.easy.result.Result;
import com.easy.service.UserService;
import com.easy.utils.JwtUtil;
import com.easy.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user/user")
@Api(tags = "C-end - User Management Interface")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    @ApiOperation("WeChat Login")
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("WeChat Login, code: {}", userLoginDTO);

        //1. Call the service to implement WeChat login functionality
        User user = userService.login(userLoginDTO);

        //2. Generate a token
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String jwt = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        //3. Package the data and return it
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(jwt)
                .build();
        return Result.success(userLoginVO);
    }
}
