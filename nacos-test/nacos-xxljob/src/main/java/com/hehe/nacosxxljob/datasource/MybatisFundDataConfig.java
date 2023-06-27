package com.hehe.nacosxxljob.datasource;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @description: 订单库
 * @Description: TODO
 * @create: 2023-01-16 17:00
 **/

@Order(6)
@Configuration
@MapperScan(basePackages = {"com.hehe.nacosxxljob.mapper.funddata"},sqlSessionTemplateRef  = "funddataSqlSessionTemplate")
public class MybatisFundDataConfig {

    @Bean(name = "funddataTransactionManager")
    @Primary
    public DataSourceTransactionManager setTransactionManager(@Qualifier("funddataDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "funddataSqlSessionFactory")
    @Primary
    public SqlSessionFactory setSqlSessionFactory(@Qualifier("funddataDataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        // 分页插件配置
       /* PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        bean.setPlugins(new Interceptor[]{paginationInterceptor});*/

        // 配置打印sql语句
        // 配置打印sql语句
        MybatisConfiguration configuration = new MybatisConfiguration();
        //configuration.setLogImpl(StdOutImpl.class);
        //开启驼峰命名
        configuration.setMapUnderscoreToCamelCase(true);
        bean.setConfiguration(configuration);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/funddata/**/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "funddataSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate setSqlSessionTemplate(@Qualifier("funddataSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
