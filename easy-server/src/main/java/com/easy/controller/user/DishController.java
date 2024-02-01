package com.easy.controller.user;

import com.easy.result.Result;
import com.easy.service.DishService;
import com.easy.vo.DishVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController("userDishController")
@RequestMapping("/user/dish")
@Api(tags = "C-end - Dish Browsing Interface")
public class DishController {

    @Autowired
    private DishService dishService;

    @GetMapping("/list")
    public Result<List<DishVO>> listDishWithFlavors(Long categoryId) {
        log.info("Query dishes and their flavors, {}", categoryId);
        List<DishVO> dishVOList = dishService.listDishWithFlavors(categoryId);
        return Result.success(dishVOList);
    }
}