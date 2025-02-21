package com.douyin.service;

import com.douyin.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    /**
     * 根据条件查询地址
     * @param addressBook
     * @return
     */
    List<AddressBook> list(AddressBook addressBook);

    /**
     * 新增地址
     * @param addressBook
     */
    void save(AddressBook addressBook);

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    AddressBook getById(Long id);

    /**
     * 修改地址
     * @param addressBook
     */
    void update(AddressBook addressBook);

    /**
     * 修改地址为默认
     * @param addressBook
     */
    void setDefault(AddressBook addressBook);

    /**
     * 删除地址
     * @param id
     */
    void deleteById(Long id);
}
