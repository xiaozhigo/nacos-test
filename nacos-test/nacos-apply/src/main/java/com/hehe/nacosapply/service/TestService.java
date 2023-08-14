package com.hehe.nacosapply.service;

import com.hehe.nacosapply.dto.Test1;
import com.hehe.nacosapply.mapper.Test1Mapper;
import com.hehe.nacosdto.fegin.UserFeginClient;
import com.hehe.nacosdto.param.TestFeginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: hyf-cloud
 * @description: test
 * @Description: TODO
 * @create: 2023-01-17 11:01
 **/

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestService {

    private final Test1Mapper test1Mapper;

    private final UserFeginClient userFeginInterFace;

    public void test(int id){
        Test1 test1 = test1Mapper.loadTest1(id);
        log.info("test1="+test1);
        Test1 test11 = test1Mapper.selectById(id);
        log.info("test11="+test11);
    }

    public void testfegin() {
        TestFeginUser test3 = userFeginInterFace.testFeginUser("测试seata");
        log.info(test3.toString());
    }


}
