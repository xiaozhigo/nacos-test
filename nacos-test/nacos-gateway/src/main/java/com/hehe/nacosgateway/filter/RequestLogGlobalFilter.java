package com.hehe.nacosgateway.filter;

import com.hehe.nacoscommon.constant.LogConstant;
import com.hehe.nacoscommon.properties.PubGlobalProperties;
import com.hehe.nacoscommon.utils.AssertUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * @description: 请求日志打印
 * @Description: TODO
 * @create: 2023-02-24 10:56
 **/

@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RequestLogGlobalFilter implements GlobalFilter, Ordered {

    private final PubGlobalProperties pubGlobalProperties;


    private static final String GET_METHOD = "GET";
    private static final String POST_METHOD = "POST";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            //未开启debug模式直接放行
            if (!pubGlobalProperties.getLogLevel().toLowerCase().equals(LogConstant.SYSTEM_LOG_LEVEL)) {
                return chain.filter(exchange);
            }
            ServerHttpRequest request = exchange.getRequest();
            URI URIPath = request.getURI();
            String path = request.getPath().value();
            String method = request.getMethodValue();
            HttpHeaders header = request.getHeaders();
            log.info("***********************************请求信息**********************************");
            log.info("getway request uri = {}", URIPath);
            log.info("getway request path = {}", path);
            log.info("getway request header = {}", header);
            if(path.contains("diamond-web-file")){
                return chain.filter(exchange);
            }

            String contentType = header.getFirst("content-type");
            if (!AssertUtil.asserbol(contentType) && contentType.contains("multipart/form-data")) {
                return chain.filter(exchange);
            }

            if (POST_METHOD.equals(method) && header.getContentLength() > 0) {
                return DataBufferUtils.join(exchange.getRequest().getBody())
                        .flatMap(dataBuffer -> {
                            byte[] bytes = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(bytes);
                            String bodyString = new String(bytes, StandardCharsets.UTF_8);
                            log.info("getway-post-request-params:{}" , bodyString);
                            exchange.getAttributes().put("POST_BODY", bodyString);
                            DataBufferUtils.release(dataBuffer);
                            Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
                                DataBuffer buffer = exchange.getResponse().bufferFactory()
                                        .wrap(bytes);
                                return Mono.just(buffer);
                            });

                            ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(
                                    exchange.getRequest()) {
                                @Override
                                public Flux<DataBuffer> getBody() {
                                    return cachedFlux;
                                }
                            };
                            log.info("****************************************************************************\n");
                            return chain.filter(exchange.mutate().request(mutatedRequest)
                                    .build());
                        });
            } else if (GET_METHOD.equals(method)) {
                MultiValueMap<String, String> queryParams = request.getQueryParams();
                log.info("getway-get-request-params:{}" , queryParams);
                log.info("****************************************************************************\n");
                return chain.filter(exchange);
            }
            log.info("****************************************************************************\n");
        } catch (Exception e) {
           log.error("打印入参错误:{}",e.getMessage());
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
