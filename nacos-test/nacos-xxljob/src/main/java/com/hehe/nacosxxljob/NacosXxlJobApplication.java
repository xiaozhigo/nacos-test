package com.hehe.nacosxxljob;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.hehe.nacosxxljob.loadbalancer.LoadBalancerWeightedRuleConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication(scanBasePackages="com.hehe")
@LoadBalancerClients(defaultConfiguration = {LoadBalancerWeightedRuleConfig.class})
public class NacosXxlJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosXxlJobApplication.class, args);
    }

    @Bean
    @LoadBalanced
    //让restTemplate使用sentinel
    @SentinelRestTemplate
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
