package com.step.config;

/**
 * Created by zhushubin  on 2019-10-29.
 * email:604580436@qq.com
 */

import com.step.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author user
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Autowired
    private ApplicationProperties properties;
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/profile/**");
        if(properties.getProfile().indexOf(":/")!=-1){
            registry.addResourceHandler("/upload/profile/**").addResourceLocations("file:"+properties.getProfile());
        }else{
            registry.addResourceHandler("/upload/profile/**").addResourceLocations("classpath:"+properties.getProfile());
        }


    }
}
