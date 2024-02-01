package com.easy.mapper;

import com.easy.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    /**
     * Get user information by openid
     *
     * @param openid
     * @return
     */
    @Select("select id, openid, name, phone, sex, id_number, avatar, create_time from user where openid = #{openid}")
    User selectByOpenid(String openid);

    /**
     * Insert data
     *
     * @param user
     */
    @Insert("insert into user (openid, name, phone, sex, id_number, avatar, create_time) " +
            "values (#{openid}, #{name}, #{phone}, #{sex}, #{idNumber}, #{avatar}, #{createTime})")
    void insert(User user);
}
