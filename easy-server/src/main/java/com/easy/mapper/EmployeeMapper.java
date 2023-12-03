package com.easy.mapper;

import com.easy.annotation.AutoFill;
import com.easy.entity.Employee;
import com.easy.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * Query employee information by username
     *
     * @param username
     * @return
     */
    @Select("select id, name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user from employee where username = #{username}")
    public Employee findByUsername(String username);

    /**
     * Add new employee
     */
    @AutoFill(OperationType.INSERT)
    @Insert("insert into employee (name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) " +
            "values (#{name}, #{username}, #{password}, #{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void insert(Employee employee);

    /**
     * Dynamic condition query
     *
     * @param name
     * @return
     */
    List<Employee> list(String name);

    /**
     * Update employee
     *
     * @param employee
     */
    @AutoFill(OperationType.UPDATE)
    void update(Employee employee);

    /**
     * Query employee information by ID
     *
     * @param id
     * @return
     */
    @Select("select * from employee where id = #{id}")
    Employee getById(Long id);
}
