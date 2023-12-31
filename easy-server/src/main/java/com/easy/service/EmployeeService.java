package com.easy.service;

import com.easy.dto.EmployeeDTO;
import com.easy.dto.EmployeeLoginDTO;
import com.easy.dto.EmployeePageQueryDTO;
import com.easy.entity.Employee;
import com.easy.result.PageResult;

public interface EmployeeService {

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
}
