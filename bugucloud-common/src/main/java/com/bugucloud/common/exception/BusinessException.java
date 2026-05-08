package com.bugucloud.common.exception;

import com.bugucloud.common.result.ResultCodeEnum;
import lombok.Getter;

/**
 * 功能描述: 基础业务异常类
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/28 - 15:05
 */

@Getter
public class BusinessException extends RuntimeException {

    private final Integer code;
    private final String message;

    // 使用枚举构建异常
    public BusinessException(ResultCodeEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMessage();
    }

    // 业务逻辑异常
    public BusinessException(String message) {
        super(message);
        this.code = ResultCodeEnum.BUSINESS_FAIL.getCode();
        this.message = message;
    }

    // 自定义消息（可以覆盖枚举的默认消息）
    public BusinessException(ResultCodeEnum exceptionEnum, String message) {
        super(message);
        this.code = exceptionEnum.getCode();
        this.message = message;
    }

    // 自定义异常码和消息
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
