package com.foling.community.exception;

/**
 * @Author foling
 * @Date2021-07-25 11:28
 * @Version 1.0
 * @Other Be happy~
 **/
public class CustomizeException extends RuntimeException{
    private String message;
    private Integer code;
    public CustomizeException(ICustomizeErrorCode errorCode){
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
