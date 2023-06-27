package com.hehe.nacosgateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @description: 跨域处理
 * @Description: TODO
 * @create: 2023-02-13 18:53
 **/

@Configuration
public class CorsConfig {
    private List<String> methods = Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS");

    /**
     * 该访问配置跨域访问执行
     * @return
     */
    @Bean
    public CorsWebFilter corsWebFilter(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //1,允许任何来源
        corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));

        if (CollectionUtils.isEmpty(methods)) {
            //3,允许任何方法
            corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
        } else {
            for (String method : methods) {
                corsConfiguration.addAllowedMethod(method);
            }
        }

        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);

        //4,允许凭证
        corsConfiguration.setAllowCredentials(true);

       UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsWebFilter(source);
    }

}
