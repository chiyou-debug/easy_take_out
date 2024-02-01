package com.easy.controller.user;

import com.easy.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("userShopController")
@Api(tags = "C-end Shop Operation Interface")
@RequestMapping("/user/shop")
public class ShopController {

    public static final String SHOP_STATUS_KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Get shop status
     *
     * @return
     */
    @ApiOperation("Get shop status")
    @GetMapping("/status")
    public Result getStatus() {
        log.info("Querying shop operation status from Redis");
        return Result.success(redisTemplate.opsForValue().get(SHOP_STATUS_KEY));
    }
}
