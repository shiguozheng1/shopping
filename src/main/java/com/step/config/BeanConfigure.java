package com.step.config;

import com.step.properties.LdapProperties;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import javax.net.SocketFactory;
import javax.servlet.MultipartConfigElement;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2019-04-17.
 */
@Component
@Slf4j
public class BeanConfigure {
    @Autowired
    private LdapProperties ldapProperties;
    private SocketFactory socketFactory;
    @Bean
    @Conditional(value= LdapCondition.class)
   public LDAPConnection ldapConnection(){
        try {


            return  new LDAPConnection(ldapProperties.getLdapHost(), ldapProperties.getLdapPort(),
                       ldapProperties.getLdapBindDN(), ldapProperties.getLdapPassword());
        } catch (LDAPException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return null;
    }
    @Bean(name = "sslLdapConnection")
    @Conditional(value= LdapCondition.class)
   public LDAPConnection sslLdapConnection(){
        try {
            SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager());
            SocketFactory socketFactory = sslUtil.createSSLSocketFactory();
            return new LDAPConnection(socketFactory,ldapProperties.getLdapHost(), 636);
        } catch (LDAPException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 校验器
     * @return
     */
    @Bean
    public Validator validator(){
        ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
                .configure()
                //开启快速校验--默认校验所有参数，false校验全部
                .addProperty( "hibernate.validator.fail_fast", "true" )
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        return validator;
    }

//    /***
//     * 自定义json 解析器
//     * @return HttpMessageConverters
//     */
//   public HttpMessageConverters  fastJsonHttpMessageConverters(){
//       FastJsonHttpMessageConverter4 fastConverter = new FastJsonHttpMessageConverter4();
//       fastConverter.setFastJsonConfig(fastJsonConfig);
//       HttpMessageConverter<?> converter = fastConverter;
//       return new HttpMessageConverters(converter);
//   }

}
