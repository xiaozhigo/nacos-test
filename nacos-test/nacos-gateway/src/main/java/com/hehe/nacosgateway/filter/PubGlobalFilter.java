package com.hehe.nacosgateway.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpServerErrorException;

/**
 * @program: hy-cloud
 * @description: fegin调用过滤器
 * @Description: TODO
 * @create: 2023-02-23 17:19
 **/

@Slf4j
@RefreshScope
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PubGlobalFilter {
    /**
     * 打印请求日志
     * @return
     */
    @Bean
    @Order(1)
    public GlobalFilter logGlobalFilter(){
        return (exchange, chain) -> {
            String rawPath = exchange.getRequest().getURI().getRawPath();
            log.info("gateway--path--log:{}",rawPath);
            //获取请求路径
            if(isPv(rawPath)){
                throw new HttpServerErrorException(HttpStatus.FORBIDDEN,"prohibit external access to the fegin API");
            }
            // 继续向下执行
            return chain.filter(exchange);
        };

    }


    /**
     * 判断是否内部私有方法
     * @param requestURI 请求路径
     * @return boolean
     */
    private boolean isPv(String requestURI) {
        return isAccess(requestURI,"/fegin");
    }

    /**
     * 网关访问控制校验
     */
    private boolean isAccess(String requestURI, String access) {
        //后端标准请求路径为 /访问控制/请求路径
        int index = requestURI.indexOf(access);
        return index >= 0 && StringUtils.countOccurrencesOf(requestURI.substring(0,index),"/") < 1;
    }

}
