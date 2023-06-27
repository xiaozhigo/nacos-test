package com.hehe.nacoscommon.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @Date: 2020/12/3 13:56
 * @Description: redission分布式锁
 * @Version 1.0.0
 */
@Slf4j
@Order(1)
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedissionConfig {

  private final RedisProperties redisProperties;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() throws IOException {
        Config config = Config.fromYAML(new ClassPathResource("application-single.yml").getInputStream());
        String[] addrstrs =redisProperties.getNodes();
        List<String> newNodes = new ArrayList(addrstrs.length);
        Arrays.stream(addrstrs).forEach((index) -> newNodes.add(
                index.startsWith("redis://") ? index : "redis://" + index));
        config.useSentinelServers()
                .addSentinelAddress(newNodes.toArray(new String[0]))
                .setMasterName(redisProperties.getMaster());
        String password = redisProperties.getPassword();
        if (StringUtils.isNotBlank(password)) {
            config.useSentinelServers().setPassword(password);
        }
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

}
