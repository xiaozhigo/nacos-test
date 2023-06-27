package com.hehe.nacosgateway.filter;

import com.hehe.nacoscommon.constant.LogConstant;
import com.hehe.nacoscommon.properties.PubGlobalProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 响应参数打印
 * @Description: TODO
 * @create: 2023-02-24 11:00
 **/

@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ResponseLogGlobalFilter  implements GlobalFilter, Ordered {

    private final PubGlobalProperties pubGlobalProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            if (pubGlobalProperties.getLogLevel().toLowerCase().equals(LogConstant.SYSTEM_LOG_LEVEL)) {
                ServerHttpResponse originalResponse = exchange.getResponse();
                DataBufferFactory bufferFactory = originalResponse.bufferFactory();

                HttpStatus statusCode = originalResponse.getStatusCode();

                if (statusCode == HttpStatus.OK) {
                    ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {

                        @Override
                        public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                            //log.info("body instanceof Flux: {}", (body instanceof Flux));
                            if (body instanceof Flux) {
                                Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                                //
                                return super.writeWith(fluxBody.map(dataBuffer -> {
                                    byte[] content = new byte[dataBuffer.readableByteCount()];
                                    dataBuffer.read(content);
                                    DataBufferUtils.release(dataBuffer);//释放掉内存
                                    // 构建日志
                                    StringBuilder sb2 = new StringBuilder(200);
                                    sb2.append("<--- {} {} \n");
                                    List<Object> rspArgs = new ArrayList<>();
                                    rspArgs.add(originalResponse.getStatusCode());
                                    //rspArgs.add(requestUrl);
                                    String data = new String(content, StandardCharsets.UTF_8);//data
                                    sb2.append(data);
                                    log.info("gateway respone: ");
                                    log.info(sb2.toString(), rspArgs.toArray());//log.info("<-- {} {}\n", originalResponse.getStatusCode(), data);
                                    return bufferFactory.wrap(content);
                                }));
                            } else {
                                log.error("<--- {} 响应code异常", getStatusCode());
                            }
                            return super.writeWith(body);
                        }
                    };
                    return chain.filter(exchange.mutate().response(decoratedResponse).build());
                }
            }
            return chain.filter(exchange);//降级处理返回数据
        }catch (Exception e){
            log.error("gateway log exception.\n" + e);
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
