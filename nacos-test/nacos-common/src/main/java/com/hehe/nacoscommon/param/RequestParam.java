package com.hehe.nacoscommon.param;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 处理request参数
 **/

@Slf4j
public class RequestParam {

    /**
     * token名称
     */
    public static final String TOKEN_NAME = "Token";

    /**
     * 渠道名称
     */
    public static final String NETNO_NAME = "netNo";

    /**
     * 版本号名称
     */
    public static final String VERSION_NAME = "version";

    /**
     * 处理请求头参数
     */
    public static ReuqestHeaderParam doReuqestHeaderParam(HttpServletRequest request){
        try {
            String token = request.getHeader(TOKEN_NAME);
            String netNo = request.getHeader(NETNO_NAME);
            String version = request.getHeader(VERSION_NAME);
            return ReuqestHeaderParam.builder()
                    .token(token)
                    .netNo(netNo)
                    .version(version)
                    .build();
        } catch (Exception e){
            log.error("ReuqestHeaderParam error:{}",e.getMessage());
            return new ReuqestHeaderParam();
        }

    }
}
