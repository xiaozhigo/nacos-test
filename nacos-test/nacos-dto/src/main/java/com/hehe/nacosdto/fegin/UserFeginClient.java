package com.hehe.nacosdto.fegin;

import com.hehe.nacosdto.fallback.FeignClientFallback;
import com.hehe.nacosdto.param.TestFeginUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "nacos-apply",contextId = "userFeginClient",fallback = FeignClientFallback.class)
public interface UserFeginClient {

    @RequestMapping("/testFeginUser")
    public TestFeginUser testFeginUser(String name);

}
