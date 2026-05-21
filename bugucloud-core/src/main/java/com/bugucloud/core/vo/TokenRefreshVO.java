package com.bugucloud.core.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 功能描述:
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/21 - 23:21
 */
@Data
@Builder
public class TokenRefreshVO {
    /** 新的访问令牌 */
    private String accessToken;

    /** 新的刷新令牌 */
    private String refreshToken;
}
