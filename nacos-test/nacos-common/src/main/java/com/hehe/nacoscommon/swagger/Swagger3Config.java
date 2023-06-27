package com.hehe.nacoscommon.swagger;

import com.hehe.nacoscommon.param.RequestParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.*;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @description: swagger 3.0
 **/
@Configuration
@EnableOpenApi //注解启动用Swagger的使用，同时在配置类中对Swagger的通用参数进行配置
public class Swagger3Config implements EnvironmentAware {

    private String applicationName;

    private String applicationDescription;

    @Bean
    public Docket createRestApi(){
        //返回文档概要信息
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .globalRequestParameters(getGlobalRequestParameters())
                .globalResponses(HttpMethod.GET,getGlobalResponseMessage())
                .globalResponses(HttpMethod.POST,getGlobalResponseMessage());
    }

    /*
    生成接口信息，包括标题，联系人等
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(applicationName+"接口文档")
                .description(applicationDescription)
                .contact(new Contact("海银基金接口文档","https://test.fundhaiyin.com/gateway/doc.html",""))
                .version("1.0")
                .build();
    }


    /*
    封装全局通用参数
     */
    private List<RequestParameter> getGlobalRequestParameters() {
        List<RequestParameter> parameters=new ArrayList<>();
        parameters.add(new RequestParameterBuilder()
                .name(RequestParam.TOKEN_NAME)
                .description("token")
                .required(false)
                .in(ParameterType.HEADER)
                .query(q->q.model(m->m.scalarModel((ScalarType.STRING))))
                .required(false)
                .build());
        parameters.add(new RequestParameterBuilder()
                .name("timeStamp")
                .description("当前时间戳")
                .required(true)
                .in(ParameterType.HEADER)
                .query(q->q.model(m->m.scalarModel((ScalarType.STRING))))
                .required(true)
                .build());
        parameters.add(new RequestParameterBuilder()
                .name(RequestParam.NETNO_NAME)
                .description("渠道")
                .required(true)
                .in(ParameterType.HEADER)
                .query(q->q.model(m->m.scalarModel((ScalarType.STRING))))
                .required(true)
                .build());
        parameters.add(new RequestParameterBuilder()
                .name(RequestParam.VERSION_NAME)
                .description("版本号")
                .required(false)
                .in(ParameterType.HEADER)
                .query(q->q.model(m->m.scalarModel((ScalarType.STRING))))
                .required(false)
                .build());
        return parameters;
    }
    /*
    封装通用相应信息
     */
    private List<Response> getGlobalResponseMessage() {
        List<Response> responseList=new ArrayList<>();
        responseList.add(new ResponseBuilder().code("HY404").description("未找到资源").build());
        return responseList;
    }

    @Bean
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(
            WebEndpointsSupplier webEndpointsSupplier, ServletEndpointsSupplier servletEndpointsSupplier,
            ControllerEndpointsSupplier controllerEndpointsSupplier, EndpointMediaTypes endpointMediaTypes,
            CorsEndpointProperties corsProperties, WebEndpointProperties webEndpointProperties, Environment environment) {
        List<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
        String basePath = webEndpointProperties.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        boolean shouldRegisterLinksMapping = webEndpointProperties.getDiscovery().isEnabled() &&
                (org.springframework.util.StringUtils.hasText(basePath) || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes, corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath), shouldRegisterLinksMapping, null);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.applicationDescription = environment.getProperty("spring.application.description");
        this.applicationName = environment.getProperty("spring.application.name");
    }
}
