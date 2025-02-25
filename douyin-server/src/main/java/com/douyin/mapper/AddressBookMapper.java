package com.douyin.mapper;


import com.douyin.entity.AddressBook;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AddressBookMapper {

    /**
     * 根据用户id查询所有地址
     * @param addressBook
     * @return
     */
    List<AddressBook> list(AddressBook addressBook);

    /**
     * 新增
     * @param addressBook
     */
    @Insert("insert into douyin.address_book" +
            "        (user_id, consignee, phone, province_name, city_name, " +
            "         district_name, detail, label, is_default)" +
            "        values (#{userId}, #{consignee}, #{phone}, #{provinceName}, #{cityName}," +
            "                #{districtName}, #{detail}, #{label}, #{isDefault})")
    void insert(AddressBook addressBook);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Select("select * from douyin.address_book where id = #{id}")
    AddressBook getById(Long id);

    /**
     * 根据id修改
     * @param addressBook
     */
    void update(AddressBook addressBook);

    /**
     * 根据 用户id修改 是否默认地址
     * @param addressBook
     */
    @Update("update douyin.address_book set is_default = #{isDefault} where user_id = #{userId}")
    void updateIsDefaultByUserId(AddressBook addressBook);

    /**
     * 根据id删除地址
     * @param id
     */
    @Delete("delete from douyin.address_book where id = #{id}")
    void deleteById(Long id);

    /**
     * 根据用户id删除地址表的记录
     * @param id
     */
    @Delete("delete from douyin.address_book where user_id = #{id}")
    void deleteByUserId(Long id);

    /**
     * 查询用户的默认地址
     * @return
     */
    @Select("select * from douyin.address_book where user_id = #{userId} and is_default = #{isDefault}")
    AddressBook getByUserIdAndDefault(@Param("userId") Long userid, @Param("isDefault") Short isDefault);

}
