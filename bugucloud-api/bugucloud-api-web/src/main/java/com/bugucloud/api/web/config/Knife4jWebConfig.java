package com.bugucloud.api.web.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 功能描述: Knife4j的配置文件
 * 访问地址：
 *      Knife4j 文档：http://localhost:8080/doc.html
 *      Swagger 原生：http://localhost:8080/swagger-ui.html
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 17:03
 */
@Configuration
public class Knife4jWebConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("布谷云项目接口文档")
                        .description("布谷云web端的接口文档")
                        .version("1.0.0"));
    }
}
