package com.step.advice;

import com.step.exception.AuthException;
import com.step.utils.wrapper.WrapMapper;
import com.step.utils.wrapper.Wrapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * shigz
 * 2019/11/4
 **/

@ControllerAdvice
public class MyControllerAdvice {
    /**
     * 拦截捕捉自定义异常 MyException.class
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = AuthException.class)
    public Wrapper authException(AuthException ex) {

        return WrapMapper.error(ex.getMessage());
    }
    @ResponseBody
    @ExceptionHandler(value = AuthenticationException.class)
    public Wrapper usernameNotFoundException(AuthenticationException ex) {

        return WrapMapper.error(ex.getMessage());
    }
    @ResponseBody
    @ExceptionHandler(value = InvalidGrantException.class)
    public Wrapper invalidGrantException(InvalidGrantException ex){
        return WrapMapper.error(ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Wrapper illegalArgumentException(IllegalArgumentException ex){
        return WrapMapper.wrap(Wrapper.ResponseType.ILLEGAL_ARGUMENT,ex.getMessage());
    }
}
