package com.hehe.nacosapply;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.hehe.nacosapply.loadbalance.LoadBalancerWeightedRuleConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableFeignClients(basePackages ="com.hehe.nacosdto.fegin")
@SpringBootApplication(scanBasePackages="com.hehe")
@LoadBalancerClients(defaultConfiguration = {LoadBalancerWeightedRuleConfig.class})
public class NacosApplyApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosApplyApplication.class, args);
    }

    @Bean
    @LoadBalanced
    //让restTemplate使用sentinel
    @SentinelRestTemplate
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
