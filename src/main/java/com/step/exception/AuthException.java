package com.step.exception;

/**
 * Created by zhushubin  on 2019-08-30.
 * email:604580436@qq.com
 */
public class AuthException extends Exception{
    private Integer code;
    public AuthException(Integer code,String messgae) {
        super(messgae);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
