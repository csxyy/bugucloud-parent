package com.bugucloud.common.util;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 功能描述:
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/26 - 14:44
 */
public class SecurityUtil {
    /**
     * 获取当前用户ID
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("用户未登录");
        }

        return (Long) authentication.getPrincipal();
    }
}
