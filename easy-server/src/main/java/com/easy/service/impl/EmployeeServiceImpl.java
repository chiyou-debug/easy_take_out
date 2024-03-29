package com.easy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easy.constant.MessageConstant;
import com.easy.constant.PasswordConstant;
import com.easy.constant.StatusConstant;
import com.easy.context.BaseContext;
import com.easy.dto.EmployeeDTO;
import com.easy.dto.EmployeeLoginDTO;
import com.easy.dto.EmployeePageQueryDTO;
import com.easy.dto.PasswordEditDTO;
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
import org.apache.commons.lang3.StringUtils;
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
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

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
        Employee employee = employeeMapper.selectOne(new LambdaQueryWrapper<Employee>()
                .eq(Employee::getUsername, employeeLoginDTO.getUsername()));

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

        // 2. Call mapper to save employee data
        employeeMapper.insert(employee);
    }

    @Override
    public PageResult page(EmployeePageQueryDTO pageQueryDTO) {
        // 1. Set pagination parameters
        IPage<Employee> page = new Page<Employee>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());

        // 2. Perform query
        employeeMapper.selectPage(page, new LambdaQueryWrapper<Employee>()
                .like(StringUtils.isNotBlank(pageQueryDTO.getName()), Employee::getName, pageQueryDTO.getName())
                .orderByDesc(Employee::getUpdateTime)
        );

        // 3. Parse and package the result
        return new PageResult(page.getTotal(), page.getRecords());
    }

    @Override
    public void enableOrDisable(Integer status, Long id) {
        Employee employee = Employee.builder()
                .id(id)
                .status(status)
                .build();
        employeeMapper.updateById(employee);
    }

    @Override
    public Employee getById(Long id) {
        return employeeMapper.selectById(id);
    }

    @Override
    public void update(EmployeeDTO employeeDTO) {
        // Entity attribute copying
        Employee employee = BeanHelper.copyProperties(employeeDTO, Employee.class);
        employeeMapper.updateById(employee);
    }

    @Override
    public void editPassword(PasswordEditDTO passwordEditDTO) {
        // 1. check the old password is matched
        Employee emp = employeeMapper.selectOne(new LambdaQueryWrapper<Employee>()
                .eq(Employee::getId, BaseContext.getCurrentId()));
        if (!emp.getPassword().equals(DigestUtils.md5DigestAsHex(passwordEditDTO.getOldPassword().getBytes()))) {
            throw new BusinessException("old password is not correct!");
        }

        // 2. change the old password to new password
        emp.setPassword(DigestUtils.md5DigestAsHex(passwordEditDTO.getNewPassword().getBytes()));
        employeeMapper.updateById(emp);
    }
}
