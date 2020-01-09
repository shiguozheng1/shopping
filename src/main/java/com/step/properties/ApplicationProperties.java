package com.step.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by zhushubin  on 2019-10-29.
 * email:604580436@qq.com
 * 应用程序配置属性
 */
@Component
@ConfigurationProperties(prefix = "application")
@Data
public class ApplicationProperties {
    /**
     * 版本
     */
    private String version;
    /**
     * 上传文件路径
     */
    private  String profile;
}
