package com.easy.controller.admin;

import com.easy.dto.DishDTO;
import com.easy.dto.DishPageQueryDTO;
import com.easy.entity.Dish;
import com.easy.result.PageResult;
import com.easy.result.Result;
import com.easy.service.DishService;
import com.easy.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(tags = "Dish Management")
@RestController
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * Add a new dish
     *
     * @return
     */
    @ApiOperation("Add a New Dish and its flavors")
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("Adding a new dish: {}", dishDTO);
        dishService.saveWithFlavors(dishDTO);
        return Result.success();
    }

    /**
     * Paginated conditional query
     *
     * @return
     */
    @ApiOperation("Paginated Conditional Query")
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("Paginated conditional query: {}", dishPageQueryDTO);
        PageResult pageResult = dishService.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Batch delete dishes
     *
     * @return
     */
    @ApiOperation("Batch Delete Dishes")
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids) {
        log.info("Batch deleting dishes: {}", ids);
        dishService.delete(ids);
        return Result.success();
    }

    /**
     * Retrieve dish information by ID for page rendering
     *
     * @return
     */
    @ApiOperation("Retrieve Dish Information by ID")
    @GetMapping("/{id}")
    public Result<DishVO> getInfo(@PathVariable Long id) {
        log.info("Retrieving dish information by ID for page rendering: {}", id);
        DishVO dishVO = dishService.getInfo(id);
        return Result.success(dishVO);
    }

    /**
     * Update a dish
     *
     * @return
     */
    @ApiOperation("Update a Dish")
    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("Updating a dish: {}", dishDTO);
        dishService.update(dishDTO);
        return Result.success();
    }

    /**
     * Enable or disable a dish for sale
     *
     * @return
     */
    @PutMapping("/status/{status}/{id}")
    @ApiOperation("Enable/Disable Dish for Sale")
    public Result startOrStop(@PathVariable Integer status, @PathVariable Long id) {
        log.info("Enabling/Disabling a dish for sale. Status: {}, ID: {}", status, id);
        dishService.startOrStop(id, status);
        return Result.success();
    }

    /**
     * Retrieve dish data by category ID
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("Retrieve Dish Data by Category ID")
    public Result<List<Dish>> list(Long categoryId) {
        log.info("Retrieving dish data for category ID: {}", categoryId);
        List<Dish> dishList = dishService.list(categoryId);
        return Result.success(dishList);
    }
}
