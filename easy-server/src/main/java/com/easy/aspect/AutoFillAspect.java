package com.easy.aspect;

import com.easy.annotation.AutoFill;
import com.easy.constant.AutoFillConstant;
import com.easy.context.BaseContext;
import com.easy.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Slf4j
@Aspect // Aspect class
@Component
public class AutoFillAspect {

    /**
     * Auto-fill properties for common fields
     */
    @Before("execution(* com.easy.mapper.*.*(..)) && @annotation(autoFill)")
    public void autoFillProperty(JoinPoint joinPoint, AutoFill autoFill) throws Exception {
        log.info("Entering AOP processing, starting to assign values to common properties");
        // 1. Get the original method's runtime arguments, take the first one (object)
        Object[] args = joinPoint.getArgs();
        if (ObjectUtils.isEmpty(args)) { // If the arguments are empty
            return;
        }
        Object obj = args[0];

        log.info("Starting to assign values to common properties, before assignment: {}", obj);

        // 2. Use reflection to obtain methods corresponding to the object's [4] common attributes
        Method setCreateTime = obj.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
        Method setUpdateTime = obj.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
        Method setCreateUser = obj.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
        Method setUpdateUser = obj.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

        // 3. Get the value property of the annotation
        OperationType operationType = autoFill.value();

        // 4. Determine if it's an insert operation, then assign values to all 4 attributes
        if (operationType.equals(OperationType.INSERT)) {
            setCreateTime.invoke(obj, LocalDateTime.now());
            setCreateUser.invoke(obj, BaseContext.getCurrentId());
        }

        setUpdateTime.invoke(obj, LocalDateTime.now());
        setUpdateUser.invoke(obj, BaseContext.getCurrentId());

        log.info("Finished assigning values to common properties, after assignment: {}", obj);
    }
}
