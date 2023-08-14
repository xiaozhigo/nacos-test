package com.hehe.nacosapply.datesource;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import io.seata.rm.datasource.DataSourceProxy;
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
 * @program: hyf-cloud
 * @description: 订单库
 * @Description: TODO
 * @create: 2023-01-16 17:00
 **/

@Order(6)
@Configuration
@MapperScan(basePackages = {"com.hehe.nacosapply.mapper"},sqlSessionTemplateRef  = "orderSqlSessionTemplate")
public class MybatisSaleOrderConfig {

    @Bean(name = "orderTransactionManager")
    @Primary
    public DataSourceTransactionManager setTransactionManager(@Qualifier("orderDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    @Primary
    @Bean(name = "orderDataSourceProxy")
    public DataSourceProxy orderDataSourceProxy(@Qualifier("orderDataSource") DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }
    @Bean(name = "orderSqlSessionFactory")
    @Primary
    public SqlSessionFactory setSqlSessionFactory(@Qualifier("orderDataSourceProxy") DataSource dataSource) throws Exception {
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
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/order/**.xml"));
        return bean.getObject();
    }

    @Bean(name = "orderSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate setSqlSessionTemplate(@Qualifier("orderSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
