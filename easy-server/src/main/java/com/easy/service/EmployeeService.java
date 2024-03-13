package com.easy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easy.dto.EmployeeDTO;
import com.easy.dto.EmployeeLoginDTO;
import com.easy.dto.EmployeePageQueryDTO;
import com.easy.dto.PasswordEditDTO;
import com.easy.entity.Employee;
import com.easy.result.PageResult;

public interface EmployeeService extends IService<Employee> {

    /**
     * Employee Login
     *
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * Add New Employee
     *
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * Page Query
     *
     * @param pageQueryDTO
     * @return
     */
    PageResult page(EmployeePageQueryDTO pageQueryDTO);

    /**
     * Enable/Disable Employee
     *
     * @param status
     * @param id
     */
    void enableOrDisable(Integer status, Long id);

    /**
     * Query Employee Information by ID
     *
     * @param id
     * @return
     */
    Employee getById(Long id);

    /**
     * Update Employee Information
     *
     * @param employeeDTO
     */
    void update(EmployeeDTO employeeDTO);

    /**
     * change old password to new password
     *
     * @param passwordEditDTO
     */
    void editPassword(PasswordEditDTO passwordEditDTO);
}
