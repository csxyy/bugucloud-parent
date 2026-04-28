package com.bugucloud.common.exception;


import com.bugucloud.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 功能描述: 全局异常处理器
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 18:39
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理所有未知系统异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("服务器异常：", e);
        return Result.error();
    }
}
