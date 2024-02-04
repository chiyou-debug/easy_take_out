package com.easy.service;

import com.easy.entity.AddressBook;

import java.util.List;

public interface AddressBookService {

    /**
     * Conditional query
     *
     * @param addressBook
     * @return
     */
    List<AddressBook> list(AddressBook addressBook);

    /**
     * Add a new address
     *
     * @param addressBook
     */
    void save(AddressBook addressBook);

    /**
     * Query by id
     *
     * @param id
     * @return
     */
    AddressBook getById(Long id);

    /**
     * Update address by id
     *
     * @param addressBook
     */
    void update(AddressBook addressBook);

    /**
     * Set default address
     *
     * @param addressBook
     */
    void setDefault(AddressBook addressBook);

    /**
     * Delete address by id
     *
     * @param id
     */
    void deleteById(Long id);

}
