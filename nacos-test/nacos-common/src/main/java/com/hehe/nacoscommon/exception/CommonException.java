package com.hehe.nacoscommon.exception;

import com.hehe.nacoscommon.constant.BizCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类 名 称：CommonException
 * 类 描 述：自定义通用异常
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonException extends  RuntimeException {
    // 序列化号
    private static final long serialVersionUID = 132719492019L;

    /**
     * 引入自定义异常消息
     */
    private BizCode resultCode;

    /**
     * 异常消息
     */
    private String msg;

    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }
}
