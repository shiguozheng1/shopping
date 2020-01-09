package com.step.config;

/**
 * Created by user on 2019-04-17.
 */
public enum ErrorCode {
    success("成功"),exist("已存在");
    private String text;
    ErrorCode(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
