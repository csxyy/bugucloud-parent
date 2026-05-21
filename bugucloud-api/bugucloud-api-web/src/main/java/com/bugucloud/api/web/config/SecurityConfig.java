package com.bugucloud.api.web.config;

import com.bugucloud.common.security.handler.CustomAccessDeniedHandler;
import com.bugucloud.common.security.handler.CustomAuthenticationEntryPoint;
import com.bugucloud.common.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 功能描述:
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/12 - 16:05
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)   // 禁用 CSRF，因为 JWT 是无状态的，不需要 Cookie 防护
                // 无状态会话，不创建 HttpSession
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 配置路径访问权限
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "news/**").permitAll()
                        .anyRequest().authenticated()                // 其他所有请求需要登录
                )
                // 将我们的 JWT 过滤器添加到 UsernamePasswordAuthenticationFilter 之前
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // 注册未登录处理器 → 返回 401 JSON
                .exceptionHandling(exc -> exc
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                // 注册权限不足处理器 → 返回 403 JSON（如果你不做角色控制，这一步可选）
                .exceptionHandling(exc -> exc
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
