package com.hehe.nacosxxljob.controller;

import com.hehe.nacosxxljob.servie.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: test
 * @Description: TODO
 * @create: 2023-01-17 11:06
 **/

@Api(tags = "hyxxljobtest")
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestController {

    private final TestService testService;

    @ApiOperation(value = "Test",notes = "Test")
    @GetMapping("/test/{id}")
    public String test(@PathVariable int id){
        testService.test(id);
        return "success";
    }
}
