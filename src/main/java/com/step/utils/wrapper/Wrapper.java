package com.step.utils.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * Created by zhushubin  on 2019-05-21.
 * email:604580436@qq.com
 */
@Data
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Wrapper<T> implements Serializable {

    public interface WrapperView{}
    /**
     * 序列化标识
     */
    private static final long serialVersionUID = -8214114563945895018L;

    /***
     * 响应数据类型
     * 成功
     * 失败
     * 参数错误
     */
    public enum ResponseType{
        SUCCESS(200,"操作成功"),ERROR(500,"内部异常"),ILLEGAL_ARGUMENT(100,"参数非法");
        private Integer code;
        private String  text;

        ResponseType(int code, String text) {
            this.code = code;
            this.text = text;
        }

        public Integer getCode() {
            return code;
        }

        public String getText() {
            return text;
        }

    }







    /**
     * 编号.
     */
    @JsonView(WrapperView.class)
    private int code;

    /**
     * 信息.
     */
    @JsonView(WrapperView.class)
    private String message;

    /**
     * 结果数据
     */
    @JsonView(WrapperView.class)
    private T result;

    /**
     * Instantiates a new wrapper. default code=200
     */
    Wrapper() {
        this(ResponseType.SUCCESS,"");
    }

    /**
     * Instantiates a new wrapper.
     *
     * @param type    响应数据类型
     * @param message the message
     */
    Wrapper(ResponseType type, String message) {
        this(type, message, null);
    }

    /**
     * Instantiates a new wrapper.
     *
     * @param type    响应数据类型
     * @param message the message
     * @param result  the result
     */
    Wrapper(ResponseType type, String message, T result) {
        super();
        if(StringUtils.isEmpty(message)){
            message = type.getText();
        }
        this.code(type.getCode()).message(message).result(result);
    }
    /**
     * Instantiates a new wrapper.
     *
     * @param type    响应数据类型
     * @param result  the result
     */
    Wrapper(ResponseType type, T result) {
      this(type,"",result);
    }

    /**
     * Sets the 编号 , 返回自身的引用.
     *
     * @param code the new 编号
     *
     * @return the wrapper
     */
    private Wrapper<T> code(int code) {
        this.setCode(code);
        return this;
    }

    /**
     * Sets the 信息 , 返回自身的引用.
     *
     * @param message the new 信息
     *
     * @return the wrapper
     */
    private Wrapper<T> message(String message) {
        this.setMessage(message);
        return this;
    }

    /**
     * Sets the 结果数据 , 返回自身的引用.
     *
     * @param result the new 结果数据
     *
     * @return the wrapper
     */
    public Wrapper<T> result(T result) {
        this.setResult(result);
        return this;
    }

    /**
     * 判断是否成功： 依据 Wrapper.SUCCESS_CODE == this.code
     *
     * @return code =200,true;否则 false.
     */
    @JsonIgnore
    public boolean success() {
        return ResponseType.SUCCESS.getCode() == this.code;
    }

    /**
     * 判断是否成功： 依据 Wrapper.SUCCESS_CODE != this.code
     *
     * @return code !=200,true;否则 false.
     */
    @JsonIgnore
    public boolean error() {
        return !success();
    }

}
