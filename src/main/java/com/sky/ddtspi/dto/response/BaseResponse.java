package com.sky.ddtspi.dto.response;

import com.sky.ddtspi.common.constant.ResponseConstant;
import com.sky.ddtspi.common.response.IResponseBaseEnum;

import java.io.Serializable;

/**
 * @author baixueping
 * @description 统一返回结果
 * @date 2021/3/23 18:14
 */
public class BaseResponse<T> implements Serializable {
    /**
     * 操作成功编码
     */
    public static final String SUCCESS_CODE = ResponseConstant.SUCCESS_CODE;
    /**
     * 200表示成功，其他自定义编码
     */
    private String code;

    /**
     * 信息
     */
    private String message;

    /**
     * 响应其他数据
     */
    private T data;

    public static String getSuccessCode() {
        return SUCCESS_CODE;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public static BaseResponse success() {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(SUCCESS_CODE);
        baseResponse.setMessage(ResponseConstant.SUCCESS_MESSAGE);
        return baseResponse;
    }

    public static BaseResponse successMessage(String message) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(SUCCESS_CODE);
        baseResponse.setMessage(message);
        return baseResponse;
    }

    public static BaseResponse success(String message, Object data) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(SUCCESS_CODE);
        baseResponse.setMessage(message);
        baseResponse.setData(data);
        return baseResponse;
    }

    public static BaseResponse successData(Object data) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(SUCCESS_CODE);
        baseResponse.setMessage(ResponseConstant.SUCCESS_MESSAGE);
        baseResponse.setData(data);
        return baseResponse;
    }

    public static BaseResponse fail() {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(ResponseConstant.FAIL_CODE);
        baseResponse.setMessage(ResponseConstant.FAIL_MESSAGE);
        return baseResponse;
    }

    public static BaseResponse failMessage(String message) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(ResponseConstant.FAIL_CODE);
        baseResponse.setMessage(message);
        return baseResponse;
    }

    public static BaseResponse fail(String code, String message) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(code);
        baseResponse.setMessage(message);
        return baseResponse;
    }
    public static BaseResponse baseEnum(IResponseBaseEnum baseEnum) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(baseEnum.getCode());
        baseResponse.setMessage(baseEnum.getMessage());
        return baseResponse;
    }

    public static BaseResponse baseEnum(IResponseBaseEnum baseEnum, Object data) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(baseEnum.getCode());
        baseResponse.setMessage(baseEnum.getMessage());
        baseResponse.setData(data);
        return baseResponse;
    }
    public boolean isSuccess() {
        return SUCCESS_CODE.equals(this.getCode());
    }

    public boolean isFail() {
        return !isSuccess();
    }
}
