package com.hehe.nacosxxljob.xxljobhandle;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @program: hy-cloud
 * @description: 测试定时任务
 * @author: loren
 * @Description: TODO
 * @create: 2023-02-16 10:26
 **/

@Slf4j
@Component
public class TestXxlJob {
    /**
     * 测试定时任务
     * @throws Exception
     */
    @XxlJob("testXxlJob")
    public void execute() throws Exception {
        log.info("this testXxlJob xxljob");
    }
}
