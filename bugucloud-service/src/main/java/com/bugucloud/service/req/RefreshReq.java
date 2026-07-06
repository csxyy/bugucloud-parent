package com.bugucloud.service.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 功能描述:
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/21 - 22:37
 */
@Data
public class RefreshReq {
    @JsonProperty("refreshToken")
    private String refreshToken;
}
