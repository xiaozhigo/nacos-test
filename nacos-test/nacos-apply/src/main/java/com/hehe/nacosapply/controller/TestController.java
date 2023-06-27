package com.hehe.nacosapply.controller;

import com.alibaba.cloud.nacos.registry.NacosServiceRegistry;
import com.alibaba.nacos.api.exception.NacosException;
import com.hehe.nacosapply.service.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.Map;

/**
 * @Description: TODO
 * @create: 2023-01-17 11:06
 **/
@Slf4j
@Api(tags = "hyproducttest")
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestController {

    private final TestService testService;
   // private final UserFeginClient userFeginInterFace;
    private final com.alibaba.cloud.nacos.NacosDiscoveryProperties NacosDiscoveryProperties;

    private final NacosServiceRegistry nacosServiceRegistry;

    @ApiOperation(value = "Test",notes = "Test")
    @GetMapping("/test/{id}")
    public String test(@PathVariable int id){
        testService.test(id);
        return "success";
    }

    @ApiOperation(value = "nacosconfig",notes = "Test")
    @GetMapping("/ns/config")
    public void nacosconfig() throws NacosException {
        Registration registration = new Registration() {
            @Override
            public String getServiceId() {
                return "test1";
            }

            @Override
            public String getHost() {
                return "10.135.1.1";
            }

            @Override
            public int getPort() {
                return 8080;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public URI getUri() {
                return null;
            }

            @Override
            public Map<String, String> getMetadata() {
                return null;
            }
        };
        nacosServiceRegistry.register(registration);
        //NacosDiscoveryProperties.namingServiceInstance().registerInstance("test","10.135.1.1",8080);
    }

    @ApiOperation(value = "testFegin",notes = "testFegin")
    @GetMapping("/testFegin")
    public String testFegin(){
        testService.testfegin();
        return "success";
    }
}
