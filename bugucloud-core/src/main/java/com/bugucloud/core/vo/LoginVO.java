package com.bugucloud.core.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 功能描述: 登录响应
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/21 - 22:52
 */
@Data
@Builder
public class LoginVO {

    /** 访问令牌  */
    private String accessToken;

    /** 刷新令牌 */
    private String refreshToken;

    /** 用户信息 */
    private UserInfoVO userInfo;
}
