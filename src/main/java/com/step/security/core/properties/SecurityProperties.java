package com.step.security.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by zhushubin  on 2019-08-28.
 * email:604580436@qq.com
 */
@ConfigurationProperties(prefix = "step.security")
@Data
public class SecurityProperties {
    /**
     * 浏览器端配置
     */
    private BrowserProperties browser = new BrowserProperties();
    /**
     * OAuth2认证服务器配置
     */
    private OAuth2Properties oauth2 = new OAuth2Properties();
}
