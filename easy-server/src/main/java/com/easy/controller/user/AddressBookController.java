package com.easy.controller.user;

import com.easy.context.BaseContext;
import com.easy.entity.AddressBook;
import com.easy.result.Result;
import com.easy.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Api(tags = "Client-side Address Book Interface")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * Query all address information of the currently logged-in user
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("Query all address information of the currently logged-in user")
    public Result<List<AddressBook>> list() {
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);
        return Result.success(list);
    }

    /**
     * Add a new address
     *
     * @param addressBook
     * @return
     */
    @PostMapping
    @ApiOperation("Add a new address")
    public Result save(@RequestBody AddressBook addressBook) {
        addressBookService.save(addressBook);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("Query address by id")
    public Result<AddressBook> getById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * Update address by id
     *
     * @param addressBook
     * @return
     */
    @PutMapping
    @ApiOperation("Update address by id")
    public Result update(@RequestBody AddressBook addressBook) {
        addressBookService.update(addressBook);
        return Result.success();
    }

    /**
     * Set default address
     *
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    @ApiOperation("Set default address")
    public Result setDefault(@RequestBody AddressBook addressBook) {
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    /**
     * Delete address by id
     *
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("Delete address by id")
    public Result deleteById(Long id) {
        addressBookService.deleteById(id);
        return Result.success();
    }

    /**
     * Query the default address
     */
    @GetMapping("/default")
    @ApiOperation("Query the default address")
    public Result<AddressBook> getDefault() {
        // SQL: select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = new AddressBook();
        addressBook.setIsDefault(1);
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);

        if (list != null && list.size() == 1) {
            return Result.success(list.get(0));
        }

        return Result.error("No default address found");
    }
}
