package com.easy.mapper;

import com.easy.annotation.AutoFill;
import com.easy.dto.CategoryPageQueryDTO;
import com.easy.entity.Category;
import com.easy.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * Insert data into the category table
     *
     * @param category
     */
    @AutoFill(OperationType.INSERT)
    @Insert("insert into category(type, name, sort, status, create_time, update_time, create_user, update_user)" +
            " VALUES" +
            " (#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Category category);

    /**
     * Paginated query of categories
     *
     * @param categoryPageQueryDTO
     * @return
     */
    List<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * Delete a category by its id
     *
     * @param id
     */
    @Delete("delete from category where id = #{id}")
    void deleteById(Long id);

    /**
     * Update a category based on its id
     *
     * @param category
     */
    @AutoFill(OperationType.UPDATE)
    void update(Category category);

    /**
     * Query categories by their type
     *
     * @param type
     * @return
     */
    List<Category> list(Integer type);

    /**
     * Query category information by its ID
     *
     * @param id
     * @return
     */
    @Select("select * from category where id = #{id}")
    Category getById(Long id);
}
