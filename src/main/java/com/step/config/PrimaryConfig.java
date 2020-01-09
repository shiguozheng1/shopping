package com.step.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.dialect.PostgreSQL9Dialect;
import org.hibernate.dialect.SQLServer2008Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef="entityManagerFactoryPrimary",
        transactionManagerRef="transactionManagerPrimary",
        basePackages= { "com.step.repository.primary" })
public class PrimaryConfig {
    @Autowired
    @Qualifier("primaryDataSource")
    private DataSource druidDataSource;
    @Autowired
    private DruidDataSourceProperties properties;
    @Bean("entityManagerFactoryPrimary")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(druidDataSource);
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());
        entityManagerFactoryBean.setJpaProperties(hibernateProperties());
        entityManagerFactoryBean.setPackagesToScan("com.step.entity.primary");
        return entityManagerFactoryBean;
    }

    private JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        String jdbcUrl =properties.getPrimary().getUrl();
        if (StringUtils.contains(jdbcUrl, ":postgresql:"))
            jpaVendorAdapter.setDatabasePlatform(PostgreSQL9Dialect.class.getName());
        else if (StringUtils.contains(jdbcUrl, ":mysql:"))
            jpaVendorAdapter.setDatabasePlatform(MySQL5InnoDBDialect.class.getName());
        else if (StringUtils.contains(jdbcUrl, ":oracle:"))
            jpaVendorAdapter.setDatabasePlatform(Oracle10gDialect.class.getName());
        else if(StringUtils.contains(jdbcUrl,":sqlserver:")){
            jpaVendorAdapter.setDatabasePlatform(SQLServer2008Dialect.class.getName());
        }
        else
            throw new IllegalArgumentException("Unknown Database of " + jdbcUrl);
        return jpaVendorAdapter;
    }
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        //properties.put("hibernate.physical_naming_strategy",new SpringPhysicalNamingStrategy());
        properties.put("hibernate.show_sql",true);
        properties.put("hibernate.enable_lazy_load_no_trans",true);
//        properties.put("hibernate.hbm2ddl.auto","update");
        return properties;
    }
    @Bean("transactionManagerPrimary")
    //@Primary
    public JpaTransactionManager transactionManager(@Qualifier("entityManagerFactoryPrimary") EntityManagerFactory
                                                                entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
