package com.hehe.nacosdto.fallback;

import com.hehe.nacosdto.fegin.UserFeginClient;
import com.hehe.nacosdto.param.TestFeginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FeignClientFallback implements UserFeginClient {


    @Override
    public TestFeginUser testFeginUser(String name) {
        log.debug("testfeginuser-time-out");
        return new TestFeginUser("降级处理",18);
    }
}
