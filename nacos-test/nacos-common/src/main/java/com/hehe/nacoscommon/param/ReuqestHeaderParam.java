package com.hehe.nacoscommon.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description: 请求头参数
 **/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReuqestHeaderParam implements Serializable {

    /**
     * token
     */
    private String token;

    /**
     * 渠道
     */
    private String netNo;

    /**
     * 版本号
     */
    private String version;
}
