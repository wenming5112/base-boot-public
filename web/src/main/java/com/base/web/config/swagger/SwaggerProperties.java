package com.base.web.config.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/5 10:14
 **/
@ConfigurationProperties(prefix = "swagger-doc")
@Component
@Data
public class SwaggerProperties {
    private String accessTokenKey;
    private String basePackage;
    private String title;
    private String description;
    private String url;
    private String version;
    private SwaggerContact contact;
}
