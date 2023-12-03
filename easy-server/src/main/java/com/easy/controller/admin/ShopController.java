package com.easy.controller.admin;

import com.easy.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/shop")
@Api(tags = "Shop Related APIs")
public class ShopController {

    public static final String SHOP_STATUS_KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    @PutMapping("/{status}")
    @ApiOperation("Set Shop Operational Status")
    public Result setStatus(@PathVariable Integer status) {
        log.info("Setting shop operational status to: {}", status == 1 ? "Operating" : "Sampling");
        redisTemplate.opsForValue().set(SHOP_STATUS_KEY, status);
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation("Retrieve Shop Operational Status")
    public Result getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(SHOP_STATUS_KEY);
        log.info("Retrieving shop operational status: {}", status == 1 ? "Operating" : "Sampling");
        return Result.success(status);
    }
}
