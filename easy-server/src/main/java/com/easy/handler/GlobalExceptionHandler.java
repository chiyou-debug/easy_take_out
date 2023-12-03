package com.easy.handler;

import com.easy.constant.MessageConstant;
import com.easy.exception.BaseException;
import com.easy.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global Exception Handler, handles business exceptions thrown within the project
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Catch business exceptions
     *
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result baseExceptionHandler(BaseException ex) {
        ex.printStackTrace();
        log.error("Exception Info: {}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * Catch DuplicateKeyException, this exception is thrown when a duplicate field (with unique constraint) occurs in the database
     *
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result DuplicateKeyExceptionHandler(DuplicateKeyException ex) {
        ex.printStackTrace();
        log.error("Exception Info: {}", ex.getMessage());
        String errorMessage = MessageConstant.UNKNOWN_ERROR;

        String message = ex.getCause().getMessage();
        if (StringUtils.hasLength(message)) {
            String[] msgs = message.split(" ");
            errorMessage = msgs[2] + " already exists";
        }

        return Result.error(errorMessage);
    }

    /**
     * Catch other exceptions
     *
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(Exception ex) {
        ex.printStackTrace();
        log.error("Exception Info: {}", ex.getMessage());
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }

}
