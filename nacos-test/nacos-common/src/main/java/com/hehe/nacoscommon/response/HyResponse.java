package com.hehe.nacoscommon.response;

import com.hehe.nacoscommon.constant.BizCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Author zzy
 * @Date 2022-09-02 14:46
 */
@Data
@ApiModel("响应返回")
public class HyResponse<T> implements Serializable {

    @ApiModelProperty("状态码")
    private String code = BizCode.COMMON_SUCCESS.getCode();

    @ApiModelProperty("状态")
    private boolean status = true;

    @ApiModelProperty("数据")
    private T data = null;

    @ApiModelProperty("附加信息")
    private String message = BizCode.COMMON_SUCCESS.getMessage();

    @ApiModelProperty(value = "时间戳")
    private long timestamp = System.currentTimeMillis();

    private HyResponse() {
    }

    private HyResponse(T data) {
        this.data = data;
    }

    private HyResponse(BizCode code) {
        this.code = code.getCode();
    }

    private HyResponse(String code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }
    private HyResponse(String code, boolean status, T data, String message) {
        this.code = code;
        this.status = status;
        this.data = data;
        this.message = message;
    }
    @Override
    public String toString() {
        return "HyResponse{" +
                "code='" + code + '\'' +
                ", data=" + data +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    public static <T> HyResponse<T> of (String code, boolean status,T data, String message) {
        return new HyResponse<>(code,status, data, message);
    }

    public static <T> HyResponse<T> success() {
        return new HyResponse<>(BizCode.COMMON_SUCCESS);
    }

    public static <T> HyResponse<T> success(T data) {
        HyResponse<T> response = new HyResponse<>(data);
        response.setCode(BizCode.COMMON_SUCCESS.getCode());
        return response;
    }

    public static <T> HyResponse<T> error(BizCode code) {
        HyResponse<T> response = new HyResponse<>(code);
        response.setMessage(code.getMessage());
        response.setStatus(false);
        return response;
    }

    public static <T> HyResponse<T> error(String code, String message) {
        return new HyResponse<>(code,false ,null, message);
    }

    public static <T> HyResponse<T> error(String message) {
        return new HyResponse<>(BizCode.COMMON_ERROR.getCode(), false ,null, message);
    }

}
