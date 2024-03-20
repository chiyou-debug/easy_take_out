package com.easy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.easy.context.BaseContext;
import com.easy.entity.AddressBook;
import com.easy.mapper.AddressBookMapper;
import com.easy.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.analysis.function.Add;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    public List<AddressBook> list(AddressBook addressBook) {
        return addressBookMapper.selectList(new LambdaQueryWrapper<AddressBook>()
                .eq(addressBook.getUserId() != null, AddressBook::getUserId, addressBook.getUserId())
                .eq(addressBook.getPhone() != null, AddressBook::getPhone, addressBook.getPhone())
                .eq(addressBook.getIsDefault() != null, AddressBook::getIsDefault, addressBook.getIsDefault()));
    }

    @Override
    public void save(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0); // Set as non-default address
        addressBook.setCreateTime(LocalDateTime.now());
        addressBook.setUpdateTime(LocalDateTime.now());
        addressBookMapper.insert(addressBook);
    }

    @Override
    public AddressBook getById(Long id) {
        AddressBook addressBook = addressBookMapper.selectById(id);
        return addressBook;
    }

    @Override
    public void update(AddressBook addressBook) {
        addressBookMapper.updateById(addressBook);
    }

    @Override
    @Transactional
    public void setDefault(AddressBook addressBook) {
        // 1. Change all addresses of the current user to non-default addresses
        addressBook.setIsDefault(0);
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookMapper.update(addressBook, new LambdaQueryWrapper<AddressBook>()
                .eq(AddressBook::getUserId, BaseContext.getCurrentId()));

        // 2. Change the current address to the default address
        addressBook.setIsDefault(1);
        addressBookMapper.updateById(addressBook);
    }

    @Override
    public void deleteById(Long id) {
        addressBookMapper.deleteById(id);
    }

}
