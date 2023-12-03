package com.easy.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "easy.jwt")
public class JwtProperties {

    /**
     * Configuration for generating JWT tokens for admin employees
     */
    private String adminSecretKey;
    private long adminTtl;
    private String adminTokenName;

    /**
     * Configuration for generating JWT tokens for user-side WeChat users
     */
    private String userSecretKey;
    private long userTtl;
    private String userTokenName;

}
