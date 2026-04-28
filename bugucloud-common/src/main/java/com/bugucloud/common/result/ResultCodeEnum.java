package com.bugucloud.common.result;

import lombok.Getter;

/**
 * 功能描述: 统一返回结果状态信息类
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 18:01
 */
@Getter
public enum ResultCodeEnum {
    // 成功
    SUCCESS(200, "成功"),


    // 参数问题
    PARAM_ERROR(400, "参数不正确"),

    // 登录、权限
    UNAUTHORIZED(401, "未登录"),
    FORBIDDEN(403, "无权限"),


    // 系统异常
    SYSTEM_ERROR(500, "系统异常，请稍后重试"),

    // 业务逻辑失败
    BUSINESS_FAIL(501, "业务处理失败"),

    // 第三方调用
    REMOTE_ERROR(502, "外部服务调用失败"),

    // 限流、降级
    FLOW_LIMIT(503, "访问过于频繁，请稍后再试");


    private final Integer code;

    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
