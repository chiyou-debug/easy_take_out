package com.easy.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.easy.constant.MessageConstant;
import com.easy.constant.PasswordConstant;
import com.easy.constant.StatusConstant;
import com.easy.context.BaseContext;
import com.easy.dto.EmployeeDTO;
import com.easy.dto.EmployeeLoginDTO;
import com.easy.dto.EmployeePageQueryDTO;
import com.easy.entity.Employee;
import com.easy.exception.BusinessException;
import com.easy.exception.DataException;
import com.easy.mapper.EmployeeMapper;
import com.easy.result.PageResult;
import com.easy.service.EmployeeService;
import com.easy.utils.BeanHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final String LOGIN_PASSWORD_ERROR_KEY = "login:error:"; // Key for marking password errors
    private static final String LOGIN_LOCK_ERROR_KEY = "login:lock:"; // Key for marking account lock

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        // Validate if the employee account is locked.
        validateAccountLock(username);

        // 1. Call Mapper to query employee information
        Employee employee = employeeMapper.findByUsername(employeeLoginDTO.getUsername());

        // 2. Check if the employee exists, if not, return error message
        if (employee == null) {
            log.info("Employee information is empty, returning error message");
            throw new DataException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 3. Check the correctness of the password, return error message if incorrect
        password = DigestUtils.md5DigestAsHex(password.getBytes()); // Encrypt the plaintext password
        if (!password.equals(employee.getPassword())) {
            log.info("Password mismatch");

            // 3.1 Mark the employee password error and set the expiration time to 5 minutes.
            redisTemplate.opsForValue().set(getKey(username), "-", 5, TimeUnit.MINUTES);

            // 3.2 Get the employee password error mark, if the count >= 5, set the account lock mark
            Set<Object> keys = redisTemplate.keys(LOGIN_PASSWORD_ERROR_KEY + username + ":*");

            if (keys != null && keys.size() >= 5) {
                log.info("Employee login password wrong more than five times within 5 minutes, lock the account for one hour");
                redisTemplate.opsForValue().set(LOGIN_LOCK_ERROR_KEY + username, "-", 1, TimeUnit.HOURS);
                throw new BusinessException(MessageConstant.LOGIN_LOCK_ERROR);
            }

            throw new BusinessException(MessageConstant.PASSWORD_ERROR);
        }

        // 4. Check the employee's status, if disabled, return error message
        if (employee.getStatus() == StatusConstant.DISABLE) {
            log.info("Login account {} is disabled, login prohibited.", employeeLoginDTO.getUsername());
            throw new BusinessException(MessageConstant.ACCOUNT_LOCKED);
        }

        return employee;
    }

    private void validateAccountLock(String username) {
        Object flag = redisTemplate.opsForValue().get(LOGIN_LOCK_ERROR_KEY + username);
        if (ObjectUtils.isNotEmpty(flag)) { // Account is locked
            log.info("Account is locked, login restricted");
            throw new BusinessException(MessageConstant.LOGIN_LOCK_ERROR_MESSAGE);
        }
    }

    // Concatenate the Redis key
    private String getKey(String username) {
        return LOGIN_PASSWORD_ERROR_KEY + username + ":" + RandomStringUtils.randomAlphabetic(5);
    }

    @Override
    public void save(EmployeeDTO employeeDTO) {
        // 1. Complete entity attributes
        Employee employee = BeanHelper.copyProperties(employeeDTO, Employee.class);
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        employee.setStatus(StatusConstant.ENABLE);

        // 2. Call mapper to save data
        employeeMapper.insert(employee);
    }

    @Override
    public PageResult page(EmployeePageQueryDTO pageQueryDTO) {
        // 1. Set pagination parameters
        PageHelper.startPage(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());

        // 2. Perform query
        List<Employee> employeeList = employeeMapper.list(pageQueryDTO.getName());

        // 3. Parse and package the result
        Page<Employee> page = (Page<Employee>) employeeList;
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void enableOrDisable(Integer status, Long id) {
        Employee employee = Employee.builder().id(id).status(status)
                .build();
        employeeMapper.update(employee);
    }

    @Override
    public Employee getById(Long id) {
        return employeeMapper.getById(id);
    }

    @Override
    public void update(EmployeeDTO employeeDTO) {
        // Entity attribute copying
        Employee employee = BeanHelper.copyProperties(employeeDTO, Employee.class);
        employeeMapper.update(employee);
    }
}
