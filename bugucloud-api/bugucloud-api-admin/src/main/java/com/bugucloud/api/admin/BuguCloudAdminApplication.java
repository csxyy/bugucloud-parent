package com.bugucloud.api.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 功能描述: 启动类
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/28 - 14:23
 */
@SpringBootApplication(
        scanBasePackages = {
                "com.bugucloud.api.admin",    // 当前模块 controller、config
                "com.bugucloud.service",    // 业务层
                "com.bugucloud.common"      // 公共组件（全局异常、工具等）
        }
)
@MapperScan("com.bugucloud.core.mapper")  // 扫描 mapper 接口
public class BuguCloudAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(BuguCloudAdminApplication.class, args);
    }
}
