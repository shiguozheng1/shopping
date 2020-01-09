package com.step.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by user on 2019-04-17.
 */
@ConfigurationProperties(prefix = "ldap")
@Data
public class LdapProperties {
   private Integer  ldapPort = 389;
   private String  ldapBindDN;
   private String  ldapHost;
   private String  ldapPassword;
   private Boolean isMove;
   private String root;
   private String dc;
}
