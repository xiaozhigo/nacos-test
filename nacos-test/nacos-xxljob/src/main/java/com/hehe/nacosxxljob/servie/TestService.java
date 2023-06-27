package com.hehe.nacosxxljob.servie;

import com.hehe.nacosxxljob.dto.funddatadb.Test2;
import com.hehe.nacosxxljob.dto.orderdb.Test1;
import com.hehe.nacosxxljob.mapper.funddata.Test2Mapper;
import com.hehe.nacosxxljob.mapper.order.Test1Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestService {

    private final Test1Mapper test1Mapper;

    private final Test2Mapper test2Mapper;

    public void test(int id){
        Test1 test1 = test1Mapper.loadTest1(id);
        System.err.println("test1="+test1);
        Test1 test11 = test1Mapper.selectById(id);
        System.err.println("test11="+test11);
        Test2 test2 = test2Mapper.loadTest2(id);
        System.err.println("test2="+test2);
        Test2 test22 = test2Mapper.selectById(id);
        System.err.println("test22="+test22);
    }
}
