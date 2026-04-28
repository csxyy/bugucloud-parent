package com.bugucloud.common.exception;

import com.bugucloud.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 功能描述: 业务异常处理器
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 18:43
 */
@Slf4j
@RestControllerAdvice
public class ServiceException {
    /**
     * 处理业务异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleBusinessException(RuntimeException e) {
        log.error("业务异常：", e);
        return Result.fail();
    }
}
