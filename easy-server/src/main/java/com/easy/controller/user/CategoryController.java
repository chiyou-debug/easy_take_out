package com.easy.controller.user;

import com.easy.entity.Category;
import com.easy.result.Result;
import com.easy.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userCategoryController")
@RequestMapping("/user/category")
@Api(tags = "C-end - Category Interface")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Query categories
     *
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("Query categories")
    public Result<List<Category>> list(Integer type) {
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}
