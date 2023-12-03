package com.easy.controller.admin;

import com.easy.constant.JwtClaimsConstant;
import com.easy.dto.EmployeeDTO;
import com.easy.dto.EmployeeLoginDTO;
import com.easy.dto.EmployeePageQueryDTO;
import com.easy.entity.Employee;
import com.easy.properties.JwtProperties;
import com.easy.result.PageResult;
import com.easy.result.Result;
import com.easy.service.EmployeeService;
import com.easy.utils.JwtUtil;
import com.easy.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Employee Management Controller
 */
@Slf4j
@Api(tags = "Employee Management Interface")
@RestController
@RequestMapping("/admin/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * Employee Login
     */
    @ApiOperation("Employee Login")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("Employee login, {}", employeeLoginDTO);
        Employee employee = employeeService.login(employeeLoginDTO);

        // Generate JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String jwt = JwtUtil.createJWT(jwtProperties.getAdminSecretKey(), jwtProperties.getAdminTtl(), claims);

        // Package result and return
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder().id(employee.getId()).name(employee.getName()).userName(employee.getUsername()).token(jwt).build();
        return Result.success(employeeLoginVO);
    }

    /**
     * Employee Logout
     */
    @ApiOperation("Employee Logout")
    @PostMapping("/logout")
    public Result logout() {
        log.info("Employee logout");
        return Result.success();
    }

    /**
     * Add Employee: Administrators can add employee information for the store in the backend management system.
     */
    @ApiOperation("Add Employee")
    @PostMapping
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("Add employee, {}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * Page Query: In the employee management page, employee information can be paginated and searched by name, using fuzzy matching for the names.
     *
     * @param pageQueryDTO
     * @return
     */
    @ApiOperation("Page Query")
    @GetMapping("/page")
    public Result<PageResult> page(EmployeePageQueryDTO pageQueryDTO) {
        log.info("Conditional pagination query, {}", pageQueryDTO);
        PageResult pageResult = employeeService.page(pageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Enable/Disable Employee: Accounts can be enabled/disabled in the backend system. Once an account is disabled, it can no longer log in to the system.
     */
    @ApiOperation("Enable/Disable Employee Account")
    @PostMapping("/status/{status}")
    public Result enableOrDisable(@PathVariable Integer status, Long id) {
        log.info("Enable/Disable employee, {}, {}", status, id);
        employeeService.enableOrDisable(status, id);
        return Result.success();
    }

    /**
     * Query Employee by ID: Query employee information by ID for display on the page.
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("Query Employee by ID")
    public Result<Employee> getById(@PathVariable Long id) {
        log.info("Query employee by ID, id:{}", id);
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * Edit Employee Information: Dynamically update employee information based on ID.
     *
     * @return
     */
    @PutMapping
    @ApiOperation("Edit Employee Information")
    public Result update(@RequestBody EmployeeDTO employeeDTO) {
        log.info("Edit employee: {}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }
}
