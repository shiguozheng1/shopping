package com.step.security.app.server;

import com.step.security.core.AuthExceptionEntryPoint;
import com.step.security.core.authorize.AuthorizeConfigManager;
import com.step.security.core.properties.SecurityConstants;
import com.step.security.core.properties.SecurityProperties;
import com.step.security.provider.GoAccessDeniedHandler;
import com.step.security.provider.GoAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableResourceServer
public class AppResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private AuthenticationFailureHandler mAuthenticationFailureHandler;

    @Autowired
    private AuthenticationSuccessHandler mAuthenticationSuccessHandler;



    @Autowired
    private SecurityProperties securityProperties;


    @Autowired
    private AuthorizeConfigManager authorizeConfigManager;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.formLogin()
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
                .successHandler(mAuthenticationSuccessHandler)
                .failureHandler(mAuthenticationFailureHandler);

           http.cors().and().csrf().disable();

        // 引用默认配置
        authorizeConfigManager.config(http.authorizeRequests()
                .mvcMatchers("/**/maycur/org/sync",
                        "/upload/profile/**",
                        "/test/**"
                        )
                .permitAll()
                .antMatchers("/upload/profile/**",  "/**/*.png").permitAll());
    }


}