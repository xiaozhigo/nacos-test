package com.hehe.nacoscommon.constant;

/**
 * 请求统一返回消息
 */
public enum BizCode {

    COMMON_SUCCESS("0000", "操作成功！"),

    COMMON_ERROR("9000", "服务器异常,请稍后重试！"),
    COMMON_REQUEST_9999("9999", "无法校验柜台非柜台"),
    COMMON_CURRENT_LIMITING("HY503", "服务器限流！"),
    COMMON_REQUEST("HY417", "参数不合法！"),
    COMMON_GATEWAY_TIME_OUT("HY504", "网关超时,请重试！"),


    APP_DOUBLE_CUST("9000", "该手机号在APP存在多个账号绑定情况,请联系客服！")
    ;
    private String code;
    private String message;

    BizCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static BizCode getBizCodeByCode(String code) {
        for (BizCode bizCode : BizCode.values()) {
            if (bizCode.code.equalsIgnoreCase(code)) {
                return bizCode;
            }
        }
        return null;
    }

    public boolean isSuccess() {
        if (BizCode.COMMON_SUCCESS.getCode().equals(this.code)) {
            return true;
        }
        return false;
    }

    public boolean isError() {
        if (BizCode.COMMON_SUCCESS.getCode().equals(this.code)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BizCode{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
