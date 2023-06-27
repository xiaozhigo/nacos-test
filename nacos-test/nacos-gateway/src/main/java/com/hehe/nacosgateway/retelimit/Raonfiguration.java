package com.hehe.nacosgateway.retelimit;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * @description: 限流
 * @Description: TODO
 * @create: 2023-02-24 13:06
 **/
@Configuration
public class Raonfiguration {
    /**
     * 按照Path限流
     * @return key
     */
    @Bean
    public KeyResolver pathKeyResolver() {
        return exchange -> Mono.just(
                exchange.getRequest()
                        .getPath()
                        .toString()
        );
    }
}