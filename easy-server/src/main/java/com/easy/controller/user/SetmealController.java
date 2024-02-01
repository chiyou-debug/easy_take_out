package com.easy.controller.user;

import com.easy.constant.StatusConstant;
import com.easy.entity.Setmeal;
import com.easy.result.Result;
import com.easy.service.SetmealService;
import com.easy.vo.DishItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Api(tags = "C-end - Setmeal Browsing Interface")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * Conditional query
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("Query setmeals by category ID")
    public Result<List<Setmeal>> list(Long categoryId) {
        Setmeal setmeal = new Setmeal();
        setmeal.setCategoryId(categoryId);
        setmeal.setStatus(StatusConstant.ENABLE);

        List<Setmeal> list = setmealService.list(setmeal);
        return Result.success(list);
    }

    /**
     * Query the list of dishes included by setmeal ID
     *
     * @param id
     * @return
     */
    @GetMapping("/dish")
    @ApiOperation("Query the list of dishes included by setmeal ID")
    public Result<List<DishItemVO>> dishList(@PathVariable("id") Long id) {
        List<DishItemVO> list = setmealService.getDishItemById(id);
        return Result.success(list);
    }

}
