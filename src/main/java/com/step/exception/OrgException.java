package com.step.exception;

/**
 * Created by zhushubin  on 2019-08-30.
 * email:604580436@qq.com
 */
public class OrgException extends Exception{
    private Object errors;
    public OrgException(String messgae){
        super(messgae);
    }
    public OrgException(String messgae,Object errors) {
        super(messgae);
        this.errors = errors;
    }

    public Object getErrors() {
        return errors;
    }
}
