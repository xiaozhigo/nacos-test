package com.hehe.nacoscommon.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Date: 2021/2/26 15:56
 * @Description: Caclechoud 配置文件
 * @Version 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties(value = "spring.redis.sentinel")
@PropertySource(value = "classpath:application-${spring.profiles.active}.yml", encoding = "UTF-8")
@Configuration
public class RedisProperties {

    private  String[] nodes;

    @Value("${spring.redis.sentinel.master}")
    private String master;

    @Value("${spring.redis.password}")
    private String password;
}
