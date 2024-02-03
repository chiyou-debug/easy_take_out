package com.easy.controller.user;

import com.easy.dto.ShoppingCartDTO;
import com.easy.entity.ShoppingCart;
import com.easy.result.Result;
import com.easy.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(tags = "Client-side Shopping Cart Interfaces")
@RestController
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("Adding to shopping cart, {}", shoppingCartDTO);
        shoppingCartService.add(shoppingCartDTO);
        return Result.success();
    }

    /**
     * Fetch shopping cart
     */
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list() {
        log.info("Fetching shopping cart");
        List<ShoppingCart> shoppingCartList = shoppingCartService.list();
        return Result.success(shoppingCartList);
    }

    /**
     * Remove an item from the shopping cart
     *
     * @return
     */
    @PostMapping("/sub")
    @ApiOperation("Remove data from shopping cart")
    public Result sub(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("Removing from shopping cart, {}", shoppingCartDTO);
        shoppingCartService.subShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    /**
     * Clear the shopping cart
     *
     * @return
     */
    @DeleteMapping("/clean")
    @ApiOperation("Clear the shopping cart")
    public Result clean() {
        log.info("Clearing shopping cart data");
        shoppingCartService.cleanShoppingCart();
        return Result.success();
    }

}
