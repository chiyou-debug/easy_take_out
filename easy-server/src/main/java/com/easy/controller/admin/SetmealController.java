package com.easy.controller.admin;

import com.easy.dto.SetmealDTO;
import com.easy.dto.SetmealPageQueryDTO;
import com.easy.result.PageResult;
import com.easy.result.Result;
import com.easy.service.SetmealService;
import com.easy.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "Set Meal Management")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * Add a new set meal
     *
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation("Add a New Set Meal")
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        log.info("Adding a new set meal: {}", setmealDTO);
        setmealService.save(setmealDTO);
        return Result.success();
    }

    /**
     * Set meal pagination query
     *
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("Set Meal Pagination Query")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("Set meal data pagination query: {}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.page(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Batch delete set meals
     *
     * @return
     */
    @DeleteMapping
    @ApiOperation("Batch Delete Set Meals")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("Batch deleting set meals: {}", ids);
        setmealService.delete(ids);
        return Result.success();
    }

    /**
     * Retrieve set meal data by ID, including the list of dishes associated with the set meal
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("Retrieve Set Meal by ID")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        log.info("Retrieving set meal data by ID: {}", id);
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }

    /**
     * Update set meal data
     *
     * @return
     */
    @PutMapping
    @ApiOperation("Update Set Meal Information")
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        log.info("Updating set meal data: {}", setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }

    /**
     * Enable/Disable set meal for sale
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("Enabling/Disabling set meal for sale. ID: {}, Status: {}", id, status);
        setmealService.startOrStop(status, id);
        return Result.success();
    }
}
