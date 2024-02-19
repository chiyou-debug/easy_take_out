package com.easy.mapper;

import com.easy.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
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
     * Insert user information
     * @param user the user object to be inserted
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into user (openid, name, phone, sex, id_number, avatar, create_time) VALUES " +
            "(#{openid}, #{name}, #{phone}, #{sex}, #{idNumber}, #{avatar}, #{createTime} )")
    void insert(User user);

    @Select("select * from user where  id = #{id}")
    public User getById(Long id);
}
