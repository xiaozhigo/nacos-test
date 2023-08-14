package com.hehe.nacosapply.datesource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;

/**
 * @program: hyf-cloud
 * @description: 订单库
 * @Description: TODO
 * @create: 2023-01-16 16:16
 **/
@Order(5)
@Configuration
public class DataSourceOrderConfig {
   // @Primary
    @Bean(name = "orderDataSource")
    @ConfigurationProperties("spring.datasource.orderdb")
    public DataSource orderDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }
   // @Primary
    /*@Bean(name = "funddataDataSource")
    @ConfigurationProperties("spring.datasource.funddatadb")
    public DataSource funddataDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }*/



}
