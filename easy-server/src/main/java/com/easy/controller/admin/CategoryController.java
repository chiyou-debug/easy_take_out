package com.easy.controller.admin;

import com.easy.dto.CategoryDTO;
import com.easy.dto.CategoryPageQueryDTO;
import com.easy.entity.Category;
import com.easy.result.PageResult;
import com.easy.result.Result;
import com.easy.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Category Management
 */
@Slf4j
@RestController
@RequestMapping("/admin/category")
@Api(tags = "Category Related Interface")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Add a new category
     *
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation("Add Category")
    public Result<String> save(@RequestBody CategoryDTO categoryDTO) {
        log.info("Adding new category data: {}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * Paginated query for categories
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("Paginated Query for Categories")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("Paginated query: {}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Delete a category by its id
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation("Delete Category")
    public Result<String> deleteById(@PathVariable Long id) {
        log.info("Deleting category: {}", id);
        categoryService.deleteById(id);
        return Result.success();
    }

    /**
     * Query by ID
     *
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("Query by ID")
    public Result<Category> getById(@PathVariable Long id) {
        log.info("Query by ID: {}", id);
        Category category = categoryService.getById(id);
        return Result.success(category);
    }

    /**
     * Modify a category
     *
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @ApiOperation("Modify Category")
    public Result<String> update(@RequestBody CategoryDTO categoryDTO) {
        categoryService.update(categoryDTO);
        return Result.success();
    }

    /**
     * Enable or disable a category
     *
     * @param status
     * @param id
     * @return
     */
    @PutMapping("/status/{status}/{id}")
    @ApiOperation("Enable or Disable Category")
    public Result<String> startOrStop(@PathVariable Integer status, @PathVariable Long id) {
        categoryService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * Query categories by type
     *
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("Query Categories by Type")
    public Result<List<Category>> list(Integer type) {
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}
