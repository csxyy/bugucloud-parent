package com.bugucloud.common.result;

import lombok.Data;
import java.io.Serializable;

/**
 * 全局统一返回结果类
 *
 * @author achen
 * @version 2.0
 * @date 2026/4/16
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    // 返回码
    private Integer code;

    // 返回消息
    private String message;

    // 返回数据
    private T data;

    // 构造私有化，只能通过静态方法创建
    private Result() {
    }

    // ==================== 成功返回 ====================
    public static <T> Result<T> ok() {
        return ok(null);
    }

    public static <T> Result<T> ok(T data) {
        return build(data, ResultCodeEnum.SUCCESS);
    }

    public static <T> Result<T> ok(String message, T data) {
        return build(200, message, data);
    }

    // ==================== 系统错误（服务器异常、BUG） ====================
    public static <T> Result<T> error() {
        return build(null, ResultCodeEnum.SYSTEM_ERROR);
    }

    public static <T> Result<T> error(String message) {
        return build(ResultCodeEnum.SYSTEM_ERROR.getCode(), message, null);
    }

    public static <T> Result<T> error(ResultCodeEnum resultCodeEnum) {
        return build(null, resultCodeEnum);
    }

    public static <T> Result<T> error(Integer code, String meg) {
        return build(code, meg, null);
    }

    // ==================== 业务失败（校验失败、参数错误、业务不通过） ====================
    public static <T> Result<T> fail() {
        return build(null, ResultCodeEnum.BUSINESS_FAIL);
    }

    public static <T> Result<T> fail(ResultCodeEnum resultCodeEnum) {
        return build(null, resultCodeEnum);
    }

    // ==================== 链式自定义消息（最常用！） ====================
    public Result<T> msg(String message) {
        this.message = message;
        return this;
    }

    // ==================== 内部构建方法 ====================
    private static <T> Result<T> build(T data, ResultCodeEnum resultCodeEnum) {
        Result<T> result = new Result<>();
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        result.setData(data);
        return result;
    }

    private static <T> Result<T> build(Integer code, String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(msg);
        result.setData(data);
        return result;
    }
}
