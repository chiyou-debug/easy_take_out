package com.easy.interceptor;

import com.easy.constant.JwtClaimsConstant;
import com.easy.context.BaseContext;
import com.easy.properties.JwtProperties;
import com.easy.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Token verification interceptor for the mini-program client
 */
@Slf4j
@Component
public class UserLoginTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Request intercepted: {}", request.getRequestURL().toString());

        //1. Get the token from the request header
        String jwt = request.getHeader(jwtProperties.getUserTokenName());

        //2. Check if the token exists, if not, do not proceed - 401
        if (!StringUtils.hasLength(jwt)) {
            log.info("Token is empty, responding with 401");
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            return false;
        }

        //3. Validate the token, if token validation fails, do not proceed - 401
        try {
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), jwt);
            log.info("Token parsed successfully: {}", claims);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());

            // Store the current user's ID
            BaseContext.setCurrentId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Token is invalid, responding with 401");
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            return false;
        }

        //4. Proceed
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        BaseContext.removeCurrentId();
    }
}
