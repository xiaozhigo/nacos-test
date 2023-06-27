package com.hehe.nacoscommon.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @description: 日志级别调整
 * @Description: TODO
 * @create: 2023-02-24 11:09
 **/

@Data
@Order(-10)
@Component
@RefreshScope
public class PubGlobalProperties {
    @Value("${log.level:info}")
    private String logLevel;
}