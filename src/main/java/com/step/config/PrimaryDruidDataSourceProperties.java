package com.step.config;

import lombok.Data;
@Data
public class PrimaryDruidDataSourceProperties extends DruidDataSourceProperties {
    private String driverClassName;
    private String url;
    private String username;
    private String password;


}
